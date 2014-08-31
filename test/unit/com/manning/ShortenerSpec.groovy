package com.manning

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Shortener)
class ShortenerSpec extends Specification {

    Shortener shortener

    def setup() {
        shortener = createValidShortener()
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

    void "destination url has to be a valid url"() {
        setup:
        shortener.destinationUrl = "abc"
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

    void "shortened key is required"() {
        setup:
        shortener.shortenerKey = null
        when:
        def shortenerIsValid = shortener.validate()

        then:
        !shortenerIsValid

        when:
        shortener.shortenerKey = "abc"
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

    private Shortener createValidShortener() {
        def now = new Date()

        new Shortener(
                destinationUrl: "http://example.com",
                shortenerKey: "abc",
                userCreated: "Dummy User",
                validFrom: now,
                validUntil: now + 1
        )
    }
}
