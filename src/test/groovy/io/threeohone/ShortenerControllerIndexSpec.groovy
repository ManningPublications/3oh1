package io.threeohone

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import io.threeohone.security.User
import spock.lang.Specification
import spock.lang.Unroll

class ShortenerControllerIndexSpec extends Specification implements ControllerUnitTest<ShortenerController>, DomainUnitTest<Shortener>{

    def populateValidParams(params) {
        assert params != null
        params["validFrom"] = new Date()
        params["validUntil"] = new Date() + 1
        params["destinationUrl"] = 'http://www.google.com'
        params["userCreated"] = new User(username: "Dummy User")
    }

    def setup() {
        controller.statisticsService = Mock(StatisticsService)
        controller.shortenerSearchService = Mock(ShortenerSearchService)
        controller.userService= Mock(UserService)
    }

    void "Test the index action returns the correct model"() {

        given:
        def shortenerList = generateActiveShorteners(3)

        when: "The index action is executed"
        controller.userService.get(_) >> null
        controller.index()

        then: "The model is correct"

        model.shortenerInstanceList == shortenerList
        model.shortenerInstanceCount == 3

        and: "the shortenerSearchService should return the correct result when requested correctly"
        1 * controller.shortenerSearchService.search("", null, _) >> shortenerList

    }


    @Unroll
    def "when #givenValidityParam validity params is set, only shorteners with validity: #expectedValidityEnum are requested from the search service"() {

        when: "expired shorteners are requested"
        params.validity = givenValidityParam
        controller.index()

        then: "the search service is requested with the expired enum"
        1 * controller.shortenerSearchService.search(_, expectedValidityEnum, _) >> generateExpiredShorteners(10)

        where:

        givenValidityParam  || expectedValidityEnum
        'expired'           || Shortener.Validity.EXPIRED
        'active'            || Shortener.Validity.ACTIVE
        'future'            || Shortener.Validity.FUTURE

    }

    def "when there is only one search result in the index action, the show page is shown"() {

        given:

        def shortenerList = generateExpiredShorteners(1)

        when:
        params.search = "searchQuery"
        controller.index()

        then: "the request is redirected"
        response.status == 302
        response.redirectedUrl == "/shortener/show/${shortenerList[0].key}"

        and: "the shortener search service returns only one result"
        1 * controller.shortenerSearchService.search(_, _, _) >> shortenerList

    }


    def "when there is only one result at all (without a search) no redirect to show is executed"() {

        when:
        params.search = null
        controller.index()

        then: "no redirect is executed"
        response.redirectedUrl == null

        and: "the search service is asked to search for shorteners by user"
        1 * controller.shortenerSearchService.search(_, _, _) >> generateExpiredShorteners(1)

    }

    def "when a userId is given, the search service is requested for this user"() {

        given: "the userId param set"
        def user = new User(id: 1, username: "userToQuery", password: "password")
        controller.userService.get(1) >> user

        params.userId = 1
        params.search = "searchString"

        when: "the index action is requested"
        controller.index()

        then: "the search service is asked to search for shorteners by user"
        1 * controller.shortenerSearchService.searchByUser("searchString",_,user,_) >> generateActiveShorteners(1)
    }


    def "when a search for the full shortenerUrl is executed, only the last part as the key is used for search by extracting the serverUrl from grailsConfig"() {

        given: "the serverUrl is set via grails application config"
        grailsApplication.config.some.config.grails.serverURL = "http://3oh1.io/"

        when: "a search with the serverUrl is executed"
        params.search = "http://3oh1.io/abc"
        controller.index()

        then: "the search service is requested with only the key"
        1 * controller.shortenerSearchService.search("http://3oh1.io/abc",_,_) >> generateActiveShorteners(1)
    }

    def "when a search for the full shortenerUrl is executed, only the last part as the key is used for search by extracting the serverUrl from request header"() {

        given: "the serverUrl is not set via grails application config"
        grailsApplication.config.some.config.grails.serverURL = null

        and: "the request Host header is set"
        request.addHeader("Host", "http://3oh1.io/")

        when: "a search with the serverUrl is executed"
        params.search = "http://3oh1.io/abc"
        controller.index()

        then: "the search service is requested with only the key"
        1 * controller.shortenerSearchService.search("abc",_,_) >> generateActiveShorteners(1)
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
                    key: 'abc' + it,
                    destinationUrl: 'http://www.twitter.com/' + it,
                    validFrom: validFrom,
                    validUntil: validUntil,
                    userId: 1
            ).save(failOnError: true)
            list.add(shortnener)
        }


        list.metaClass.getTotalCount = { list.size() }

        return list

    }

}
