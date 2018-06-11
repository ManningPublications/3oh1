package io.threeohone

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class RedirectLogSpec extends Specification implements DomainUnitTest<RedirectLog>{
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



    void "a log has a creation autoTimestamp feature activated"() {

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
