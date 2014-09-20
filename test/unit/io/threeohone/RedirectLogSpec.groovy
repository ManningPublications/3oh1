package io.threeohone

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RedirectLog)
class RedirectLogSpec extends Specification {
    RedirectLog log

    def setup() {
        log = createValidRedirectLog()
    }

    void "shortener reference is required"() {

        given:
        log.shortener = null

        when:
        def logIsValid = log.validate()

        then:
        !logIsValid

        when:
        log.shortener = new Shortener()
        logIsValid = log.validate()

        then:
        logIsValid
    }

    void "a client ip is required"() {

        given:
        log.clientIp = null

        when:
        def logIsValid = log.validate()

        then:
        !logIsValid

        when:
        log.clientIp = "127.0.0.1"
        logIsValid = log.validate()

        then:
        logIsValid
    }

    void "a referer is not required"() {

        given:
        log.referer = null

        when:
        def logIsValid = log.validate()

        then:
        logIsValid

    }


    void "a userAgent is required"() {

        given:
        log.userAgent = null

        when:
        def logIsValid = log.validate()

        then:
        !logIsValid

        when:
        log.userAgent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
        logIsValid = log.validate()

        then:
        logIsValid
    }



    def "a log has a creation autoTimestamp feature activated"() {

        expect:
        log.hasProperty('dateCreated')
    }




    private RedirectLog createValidRedirectLog() {

        new RedirectLog(
                shortener: new Shortener(),
                clientIp: "127.0.0.1",
                referer: "http://www.google.com",
                userAgent: "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
        )
    }
}
