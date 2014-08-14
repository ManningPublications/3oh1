package com.manning

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RedirectFinderService)
@Mock(Shortener)
class RedirectFinderServiceSpec extends Specification {


    void 'a valid shortenerKey will find the correct destination url'() {
        setup:
        def expectedDestinationUrl = 'http://example.com'
        createShortener(shortenerKey: 'abc', destinationUrl: expectedDestinationUrl)

        when:
        def actualDestinationUrl = service.findRedirectionUrlForKey('abc')

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
