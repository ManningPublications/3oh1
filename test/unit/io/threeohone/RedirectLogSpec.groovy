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



    def "a log has a creation autoTimestamp feature activated"() {

        expect:
        log.hasProperty('dateCreated')
    }




    private RedirectLog createValidRedirectLog() {

        new RedirectLog(
                shortener: new Shortener(),
                clientIp: "127.0.0.1",
                referer: "http://www.google.com",
                clientInformation: new ClientInformation(
                        browserName: "Chrome 38",
                        browserVersion: "38.0.1.34",
                        operatingSystem: "Mac OS X",
                        mobileBrowser: false
                )
        )
    }
}
