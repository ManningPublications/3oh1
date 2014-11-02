package io.threeohone

import grails.orm.PagedResultList
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import io.threeohone.security.User
import org.hibernate.Criteria
import spock.lang.Specification

@TestFor(ShortenerController)
@Mock([Shortener, RedirectLog])
class ShortenerControllerIndexSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params["validFrom"] = new Date()
        params["validUntil"] = new Date() + 1
        params["destinationUrl"] = 'http://www.google.com'
        params["userCreated"] = new User(username: "Dummy User")
    }

    def setup() {

        // the shortener taglib is mocked
        controller.metaClass.shortener = [shortUrl: { "shortUrl" }]

        controller.statisticsService = Mock(StatisticsService)
    }

    void "Test the index action returns the correct model"() {
        setup:
        def shortenerList = generateActiveShorteners(3)

        def pagedListMock = createPagedResultListMock(shortenerList)

        controller.shortenerSearchService = Mock(ShortenerSearchService) {
            1 * search(_, _, _) >> pagedListMock
        }

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        model.shortenerInstanceList == shortenerList
        model.shortenerInstanceCount == 3
    }



    def "when active validity params is set, only active results are shown"() {

        setup: 'create and save 12 shorteners'

        def expectedCountOfActiveShorteners = 10

        generateExpiredShorteners(1)
        def shortenerList = generateActiveShorteners(expectedCountOfActiveShorteners)
        generateFutureShorteners(1)

        params.validity = 'active'


        def pagedListMock = createPagedResultListMock(shortenerList)

        controller.shortenerSearchService = Mock(ShortenerSearchService) {
            1 * search(_, _, _) >> pagedListMock
        }

        when:
        controller.index(100)

        def actualShorteners = model.shortenerInstanceList
        then:
        actualShorteners.size() == expectedCountOfActiveShorteners
        model.shortenerInstanceCount == expectedCountOfActiveShorteners

        and:
        def now = new Date()
        actualShorteners.each {
            assert it.validFrom < now
            assert it.validUntil > now
        }

    }

    def "when future validity params is set, only future results are shown"() {

        given:

        def expectedCountOfFutureShorteners = 10

        generateExpiredShorteners(1)
        def shortenerList = generateFutureShorteners(expectedCountOfFutureShorteners)
        generateActiveShorteners(1)

        params.validity = 'future'


        def pagedListMock = createPagedResultListMock(shortenerList)

        controller.shortenerSearchService = Mock(ShortenerSearchService) {
            1 * search(_, _, _) >> pagedListMock
        }

        when:
        controller.index(100)

        def actualShorteners = model.shortenerInstanceList
        then:
        actualShorteners.size() == expectedCountOfFutureShorteners

        and:
        def now = new Date()
        actualShorteners.each {
            assert it.validFrom > now
        }

    }

    def "when expired validity params is set, only expired results are shown"() {

        given:

        def expectedCountOfExpiredShorteners = 10

        def shortenerList = generateExpiredShorteners(expectedCountOfExpiredShorteners)
        generateFutureShorteners(1)
        generateActiveShorteners(1)

        params.validity = 'expired'


        def pagedListMock = createPagedResultListMock(shortenerList)

        controller.shortenerSearchService = Mock(ShortenerSearchService) {
            1 * search(_, _, _) >> pagedListMock
        }

        when:
        controller.index(100)

        def actualShorteners = model.shortenerInstanceList
        then:
        actualShorteners.size() == expectedCountOfExpiredShorteners

        and:
        def now = new Date()
        actualShorteners.each {
            assert it.validUntil < now
        }

    }

    def "when there is only one search result in the index action, the show page is shown"() {

        given:
        def shortenerList = generateExpiredShorteners(1)
        def expectedShortener = shortenerList[0]

        controller.shortenerSearchService = Mock(ShortenerSearchService) {
            1 * search(_, _, _) >> [expectedShortener]
        }
        when:
        params.search = "searchQuery"
        controller.index()

        then:
        response.status == 302
        response.redirectedUrl == "/shorteners/${expectedShortener.id}"

    }

    def "when there is only one result at all (without a search) no redirect to show is executed"() {

        given:
        def shortenerList = generateExpiredShorteners(1)

        def pagedListMock = createPagedResultListMock(shortenerList)

        controller.shortenerSearchService = Mock(ShortenerSearchService) {
            1 * search(_, _, _) >> pagedListMock
        }

        when:
        params.search = null
        controller.index()

        then:
        response.redirectedUrl == null

    }


    def "when a search for the full shortenerUrl is executed, only the last part as the shortenerKey is used for search by extracting the serverUrl from grailsConfig"() {

        given:
        controller.shortenerSearchService = Mock(ShortenerSearchService)
        controller.grailsApplication = [config: [grails: [serverURL: "http://3oh1.io/"]]]

        when:
        params.search = "http://3oh1.io/abc"
        controller.index()

        then:
        1 * controller.shortenerSearchService.search("abc",_,_) >> createPagedResultListMock(generateActiveShorteners(1))
    }

    def "when a search for the full shortenerUrl is executed, only the last part as the shortenerKey is used for search by extracting the serverUrl from request header"() {

        given:
        controller.shortenerSearchService = Mock(ShortenerSearchService)

        controller.grailsApplication = [config: [grails: [serverURL: null]]]
        request.addHeader("Host", "http://3oh1.io/")

        when:
        params.search = "http://3oh1.io/abc"
        controller.index()

        then:
        1 * controller.shortenerSearchService.search("abc",_,_) >> createPagedResultListMock(generateActiveShorteners(1))
    }


    private def createPagedResultListMock(shortenerList) {
        def mockC = mockFor(Criteria)
        mockC.demand.list { return [] } //PagedResultList constructor calls this
        def pagedList = new PagedResultList(null, mockC.createMock()) {
            {
                list = shortenerList
                totalCount = shortenerList.size()
            }
        }
        pagedList
    }

    def generateActiveShorteners(int count) {
        generateShorteners(new Date() - 1, new Date() + 1, count)
    }

    def generateExpiredShorteners(int count) {
        generateShorteners(new Date() - 2, new Date() - 1, count)
    }

    def generateFutureShorteners(int count) {
        generateShorteners(new Date() + 1, new Date() + 2, count)
    }

    def generateShorteners(Date validFrom, Date validUntil, int count) {

        def list = []

        count.times {
            def shortnener = new Shortener(
                    shortenerKey: 'abc' + it,
                    destinationUrl: 'http://www.twitter.com/' + it,
                    validFrom: validFrom,
                    validUntil: validUntil,
                    userCreated: new User(username: "Dummy User")
            ).save(failOnError: true)
            list.add(shortnener)
        }

        list
    }

}
