package com.manning

import com.manning.security.User
import grails.test.mixin.*
import org.grails.plugins.quickSearch.QuickSearchService
import spock.lang.*

@TestFor(ShortenerController)
@Mock([Shortener])
class ShortenerControllerSpec extends Specification {

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
    }

    void "Test the index action returns the correct model"() {
        setup:
        def shortenerList = generateActiveShorteners(3)

        def quickSearchService = Mock(QuickSearchService){
            1 * search(_) >> shortenerList
        }
        controller.quickSearchService = quickSearchService

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        model.shortenerInstanceList == shortenerList
        model.shortenerInstanceCount == 3
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.shortenerInstance != null
    }

    void "Test the save action does not persists an invalid instance"() {

        given:
        controller.shortenerService = Mock(ShortenerService)

        def invalidShortener = new Shortener()
        invalidShortener.validate()

        1 * controller.shortenerService.createShortener(_) >> invalidShortener

        when: "The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'

        params.destinationUrl = "notAValidDomain"
        controller.save()

        then: "The create view is rendered again with the correct model"
        model.shortenerInstance != null
        view == 'create'

    }

    void "Test the save action correctly persists an instance"() {

        given: "a form is send"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'

        and: "a Mock for a shortener Service is used"
        controller.shortenerService = Mock(ShortenerService)

        and: "a valid shortener is returned from the service mock"
        populateValidParams(params)
        def validShortener = new Shortener(params)

        validShortener.validate()
        validShortener.id = 1

        1 * controller.shortenerService.createShortener(params) >> validShortener

        when: "The save action is executed with a valid instance"
        controller.save()

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/shortener/show/1'
        controller.flash.message != null

    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def shortener = new Shortener(params)
        controller.show(shortener)

        then: "A model is populated containing the domain instance"
        model.shortenerInstance == shortener
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def shortener = new Shortener(params)
        controller.edit(shortener)

        then: "A model is populated containing the domain instance"
        model.shortenerInstance == shortener
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/shortener/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def shortener = new Shortener()
        shortener.validate()
        controller.update(shortener)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.shortenerInstance == shortener

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        shortener = new Shortener(params).save(flush: true)
        controller.update(shortener)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/shortener/show/$shortener.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/shortener/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def shortener = new Shortener(params).save(flush: true)

        then: "It exists"
        Shortener.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(shortener)

        then: "The instance is deleted"
        Shortener.count() == 0
        response.redirectedUrl == '/shortener/index'
        flash.message != null
    }

    def "when no validity params is set, active is used"() {

        setup:
        params.validity = null

        def shortenerList = generateActiveShorteners(3)

        def quickSearchService = Mock(QuickSearchService){
            1 * search(_) >> shortenerList
        }
        controller.quickSearchService = quickSearchService

        when:
        controller.index(10)

        then:
        params.validity == 'active'

    }


    def "when active validity params is set, only active results are shown"() {
        setup: 'create and save 12 shorteners '

        def expectedCountOfActiveShorteners = 10

        generateExpiredShorteners(1)
        def shortenerList = generateActiveShorteners(expectedCountOfActiveShorteners)
        generateFutureShorteners(1)

        params.validity = 'active'

        def quickSearchService = Mock(QuickSearchService) {
            1 * search(_) >> shortenerList
        }
        controller.quickSearchService = quickSearchService


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

        def quickSearchService = Mock(QuickSearchService) {
            1 * search(_) >> shortenerList
        }
        controller.quickSearchService = quickSearchService


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

        def quickSearchService = Mock(QuickSearchService) {
            1 * search(_) >> shortenerList
        }
        controller.quickSearchService = quickSearchService

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
