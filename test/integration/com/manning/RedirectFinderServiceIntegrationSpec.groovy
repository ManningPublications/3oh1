package com.manning


import spock.lang.*

class RedirectFinderServiceIntegrationSpec extends Specification {


    private service

    def setup() {
        service  = new RedirectFinderService()
    }

    void 'a valid shortenerKey will find the correct destination url'() {

        setup:

        def expectedDestinationUrl = 'http://example.com'
        createShortener(shortenerKey: 'abc', destinationUrl: expectedDestinationUrl)

        this.service = new RedirectFinderService()
        when:
        def actualDestinationUrl = this.service.findRedirectionUrlForKey('abc')

        then:
        actualDestinationUrl == expectedDestinationUrl
    }


    void 'a invalid shortenerKey will find the no destination url'() {

        setup:
        createShortener(shortenerKey: 'abc')

        when:
        def destinationUrl = service.findRedirectionUrlForKey('invalidKey')

        then:
        !destinationUrl
    }

    void "an active shortener with no validUntil value set will be redirected"() {

        given:
        createShortener(validUntil: null)

        when:
        def destinationUrl = service.findRedirectionUrlForKey('abc')

        then:
        destinationUrl
    }


    void 'a future shortener will find no destination url'() {

        setup:
        def tomorrow = new Date() + 1
        createShortener(shortenerKey: 'abc', validFrom: tomorrow)

        when:
        def destinationUrl = service.findRedirectionUrlForKey('abc')

        then:
        !destinationUrl
    }

    void 'a past shortenerKey will find no destination url'() {

        setup:
        def yesterday = new Date() - 1
        createShortener(shortenerKey: 'abc', validUntil: yesterday)

        when:
        def destinationUrl = service.findRedirectionUrlForKey('abc')

        then:
        !destinationUrl
    }


    def createShortener(Map params) {
        def now = new Date()

        def defaultValues = [
                shortenerKey: 'abc',
                destinationUrl: 'http://example.com',
                userCreated: 'Dummy user',
                validFrom: now,
                validUntil: now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }
}
