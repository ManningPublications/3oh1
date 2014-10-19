package io.threeohone

import io.threeohone.security.User
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Shortener)
class ShortenerSpec extends Specification {

    Shortener shortener
    Date yesterday
    Date tomorrow
    Date now

    def setup() {
        shortener = createValidShortener()

        now = new Date()
        yesterday = now - 1
        tomorrow = now + 1
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

    @Unroll
    void "#shortenerKey is not allowed as the shortenerKey due to the blacklist"() {

        when: "validation is executed with the current shortenerKey"
        shortener.shortenerKey = shortenerKey
        def actualValidationResult = shortener.validate()

        then: "the validation result is correct"
        expectedValidationResult == actualValidationResult

        where:

        shortenerKey || expectedValidationResult
        "shorteners" || false
        "api"        || false
        "statistics" || false
        "users"      || false
        "docs"       || false

    }

    void "a valid until date is not required"() {
        given:
        shortener.validUntil = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid

    }


    void "userCreated is required"() {

        given:
        shortener.userCreated = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        !shortenerIsValid

        when:
        shortener.userCreated = new User(username: "Dummy User")
        shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid
    }


    def "a shortener has autoTimestamping feature activated due to the existence of dateCreated and lastUpdated"() {

        expect:
        shortener.hasProperty('dateCreated') && shortener.hasProperty('lastUpdated')
    }


    def "a shortener is started when a validFrom is before now"() {

        when:
        shortener = new Shortener(validFrom: yesterday)

        then:
        shortener.isStarted()

        when:
        shortener = new Shortener(validFrom: tomorrow)

        then:
        !shortener.isStarted()

    }

    def "a shortener is ended when a validUntil is before now"() {

        when:
        shortener = new Shortener(validUntil: yesterday)

        then:
        shortener.isEnded()

        when:
        shortener = new Shortener(validUntil: tomorrow)

        then:
        !shortener.isEnded()

    }

    def "if a shortener has no validUntil set, it is never ended"() {

        when:
        shortener = new Shortener(validUntil: null)

        then:
        !shortener.isEnded()

    }

    def "a shortener is active, if is has started yesterday and ends tomorrow"() {

        expect:
        new Shortener(validFrom: yesterday, validUntil: tomorrow).isActive()

    }

    def "a shortener is active if it is already started and has no end"() {

        expect:
        new Shortener(validFrom: yesterday).isActive()
    }

    def "a shortener is not active if it has not started"() {

        when:
        shortener = new Shortener(validFrom: tomorrow)

        then:
        !shortener.isActive()
    }

    def "a shortener is not active if it has ended"() {

        when:
        shortener = new Shortener(validFrom: tomorrow)

        then:
        !shortener.isActive()
    }


    private Shortener createValidShortener() {
        def now = new Date()

        new Shortener(
                destinationUrl: "http://example.com",
                shortenerKey: "abc",
                userCreated: new User(username: "Dummy User"),
                validFrom: now,
                validUntil: now + 1
        )
    }
}
