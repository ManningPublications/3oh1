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

        given:
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

        given:
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

    /**
     *  this is because the key will be generated after the shortener was saved.
     *  It is derived from the id, that is provided by the db
     */
    void "shortenerKey is not required"() {

        given:
        shortener.shortenerKey = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid

    }

    void "a valid until date is not required"() {
        given:
        shortener.validUntil = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid

    }

    def "a shortener has autoTimestamping feature activated due to the existence of dateCreated and lastUpdated"() {

        expect:
        shortener.hasProperty('dateCreated') && shortener.hasProperty('lastUpdated')
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
