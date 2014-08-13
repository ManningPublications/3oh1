package com.manning

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RedirectController)
@Mock(Shortener)
class RedirectControllerSpec extends Specification {


    void "if no shortenerKey is given a redirect to manning.com will be executed"() {

        when:
        params.shortenerKey = null
        controller.index()

        then:
        response.redirectedUrl == 'http://www.manning.com'
        response.status == 301
    }



    void "for a valid shortener entry a redirect should occur the destination url"() {

        setup:
        def shortenerKey = 'abc'
        def expectedDestinationUrl = 'http://www.example.com'

        createShortener(shortenerKey, expectedDestinationUrl)

        when:
        params.shortenerKey = shortenerKey
        controller.index()

        then:
        response.redirectedUrl == expectedDestinationUrl
        response.status == 301

    }

    def createShortener(String shortenerKey, String destinationUrl) {
        def now = new Date()
        new Shortener(
                shortenerKey: shortenerKey,
                destinationUrl: destinationUrl,
                userCreated: "Dummy user",
                validFrom: now,
                validUntil: now + 1
        ).save(failOnError: true)
    }
}
