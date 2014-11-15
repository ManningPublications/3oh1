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

    void "a referer is not required"() {

        given:
        log.referer = null

        when:
        def logIsValid = log.validate()

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
                referer: "http://www.google.com"
        )
    }
}
