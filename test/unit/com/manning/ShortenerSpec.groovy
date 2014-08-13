package com.manning

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Shortener)
class ShortenerSpec extends Specification {

    Shortener shortener

    def setup() {
        shortener = createValidShortenedUrl()
    }

    void "destination url is required"() {
        setup:
        shortener.destinationUrl = null
        when:
        def shortenerIsValid = shortener.validate()

        then:
        !shortenerIsValid

        when:
        shortener.destinationUrl = "http://example.com"
        shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid
    }

    void "shortened url is required"() {
        setup:
        shortener.shortenedUrl = null
        when:
        def shortenerIsValid = shortener.validate()

        then:
        !shortenerIsValid

        when:
        shortener.shortenedUrl = "http://mng.bz/123"
        shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid
    }

    void "a valid until date is not required"() {
        setup:
        shortener.validUntil = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid

    }

    private Shortener createValidShortenedUrl() {
        def now = new Date()

        new Shortener(
                destinationUrl: "http://example.com",
                shortenedUrl: "http://mng.bz/123",
                userCreated: "Dummy User",
                validFrom: now,
                validUntil: now + 1
        )
    }
}
