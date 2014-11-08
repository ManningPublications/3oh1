package io.threeohone

import io.threeohone.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.geeks.browserdetection.UserAgentIdentService
import spock.lang.Specification


@TestFor(RedirectLoggingService)
@Mock([User, Shortener, RedirectLog])
class RedirectLoggingServiceSpec extends Specification {

    void "log creates a log entry in the database if valid attributes are provided"() {

        given: "i have a shortener instance that is redirected"
        def redirectedShortener = createShortener(destinationUrl: "http://www.example.com")

        service.userAgentIdentService = Mock(UserAgentIdentService)

        and:
        service.userAgentIdentService.getBrowser() >> "Chrome 38"
        service.userAgentIdentService.getBrowserVersion() >> "38.0.1.34"
        service.userAgentIdentService.getOperatingSystem() >> "Mac OS X"

        when:
        service.log(
                shortener: redirectedShortener,
                clientIp: "127.0.0.1",
                referer: "http://www.google.com"
        )
        def createdLog = RedirectLog.first()

        then:
        createdLog.shortener == redirectedShortener
        createdLog.clientIp == "127.0.0.1"
        createdLog.referer == "http://www.google.com"

        and:
        createdLog.clientInformation.browserName == "Chrome 38"
        createdLog.clientInformation.browserVersion == "38.0.1.34"
        createdLog.clientInformation.operatingSystem == "Mac OS X"


    }


    def createShortener(Map params) {
        def now = new Date()

        def defaultValues = [
                key: 'abc',
                destinationUrl: 'http://example.com',
                userCreated: new User(username: "user", password: "user", enabled: true),
                validFrom: now,
                validUntil: now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }
}
