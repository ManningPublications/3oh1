package io.threeohone

import io.threeohone.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(RedirectLoggingService)
@Mock([User, Shortener, RedirectLog])
class RedirectLoggingServiceSpec extends Specification {

    void "log creates a log entry in the database if valid attributes are provided"() {

        given: "i have a shortener instance that is redirected"
        def redirectedShortener = createShortener(destinationUrl: "http://www.example.com")

        when:
        service.log(
                shortener: redirectedShortener,
                clientIp: "127.0.0.1",
                referer: "http://www.google.com",
                userAgent: "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
        )
        def createdLog = RedirectLog.first()

        then:
        createdLog.shortener == redirectedShortener
        createdLog.clientIp == "127.0.0.1"
        createdLog.referer == "http://www.google.com"
        createdLog.userAgent == "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"

    }


    def createShortener(Map params) {
        def now = new Date()

        def defaultValues = [
                shortenerKey: 'abc',
                destinationUrl: 'http://example.com',
                userCreated: new User(username: "user", password: "user", enabled: true),
                validFrom: now,
                validUntil: now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }
}
