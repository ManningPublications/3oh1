package io.threeohone

import grails.testing.gorm.DomainUnitTest
import io.threeohone.security.User
import spock.lang.Specification
import spock.lang.Unroll

class ShortenerSpec extends Specification implements DomainUnitTest<Shortener>{

    Shortener shortener
    Date yesterday
    Date tomorrow
    Date now

    def setup() {
        mockDomains(RedirectLog)
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
    void "key is not required"() {

        given:
        shortener.key = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid

    }

    @Unroll
    void "#key is not allowed as the key due to the blacklist"() {

        when: "validation is executed with the current key"
        shortener.key = key
        def actualValidationResult = shortener.validate()

        then: "the validation result is correct"
        expectedValidationResult == actualValidationResult

        where:

        key || expectedValidationResult
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


    void "userCreated is not required"() {

        given:
        shortener.userCreated = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid

        when:
        shortener.userCreated = new User(username: "Dummy User")
        shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid
    }

    void "userId is not required"() {

        given:
        shortener.userId = null

        when:
        def shortenerIsValid = shortener.validate()

        then:
        !shortenerIsValid

        when:
        shortener.userId = 3
        shortenerIsValid = shortener.validate()

        then:
        shortenerIsValid
    }

    void "a shortener has autoTimestamping feature activated due to the existence of dateCreated and lastUpdated"() {

        expect:
        shortener.hasProperty('dateCreated') && shortener.hasProperty('lastUpdated')
    }


    void "a shortener is started when a validFrom is before now"() {

        when:
        shortener = new Shortener(validFrom: yesterday)

        then:
        shortener.isStarted()

        when:
        shortener = new Shortener(validFrom: tomorrow)

        then:
        !shortener.isStarted()

    }

    void "a shortener is ended when a validUntil is before now"() {

        when:
        shortener = new Shortener(validUntil: yesterday)

        then:
        shortener.isEnded()

        when:
        shortener = new Shortener(validUntil: tomorrow)

        then:
        !shortener.isEnded()

    }

    void "if a shortener has no validUntil set, it is never ended"() {

        when:
        shortener = new Shortener(validUntil: null)

        then:
        !shortener.isEnded()

    }

    void "a shortener is active, if is has started yesterday and ends tomorrow"() {

        expect:
        new Shortener(validFrom: yesterday, validUntil: tomorrow).isActive()

    }

    void "a shortener is active if it is already started and has no end"() {

        expect:
        new Shortener(validFrom: yesterday).isActive()
    }

    void "a shortener is not active if it has not started"() {

        when:
        shortener = new Shortener(validFrom: tomorrow)

        then:
        !shortener.isActive()
    }

    void "a shortener is not active if it has ended"() {

        when:
        shortener = new Shortener(validFrom: tomorrow)

        then:
        !shortener.isActive()
    }

    void "Test shortener delete with a linked redirectLog"() {
        given:
        RedirectLog redirectLog = new RedirectLog(referer: "aReferer", shortener: shortener).save(flush:true)

        when:
        shortener.delete()

        then:
        Shortener.count() == 0
    }



    private Shortener createValidShortener() {
        def now = new Date()

        new Shortener(
                destinationUrl: "http://example.com",
                key: "abc",
                userCreated: new User(username: "Dummy User"),
                validFrom: now,
                validUntil: now + 1
        )
    }
}
