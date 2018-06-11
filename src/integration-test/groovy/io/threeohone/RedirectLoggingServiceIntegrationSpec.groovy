package io.threeohone

import com.maxmind.geoip.LookupService
import io.threeohone.security.User
import org.geeks.browserdetection.UserAgentIdentService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class RedirectLoggingServiceIntegrationSpec extends Specification {


    RedirectLoggingService service
    Shortener redirectedShortener
    HttpServletRequest request

    LookupService geoIpService


    def setup() {
        service = new RedirectLoggingService()
        service.geoIpService = geoIpService

        redirectedShortener = createShortener(destinationUrl: "http://www.example.com")
        service.userAgentIdentService = Mock(UserAgentIdentService)

        request = Mock(HttpServletRequest)
        request.getHeader("referer") >> "http://www.google.com"


    }

    void "log logs the correct redirect information"() {

        given:
        request.getRemoteAddr() >> ""

        when:
        service.log(redirectedShortener, request)

        def log = RedirectLog.first()

        then:
        log.shortener == redirectedShortener
        log.referer == "http://www.google.com"


    }

    void "log logs the correct clientInformation"() {

        given:
        service.userAgentIdentService.getBrowser() >> "Chrome 38"
        service.userAgentIdentService.getBrowserVersion() >> "38.0.1.34"
        service.userAgentIdentService.getOperatingSystem() >> "Mac OS X"

        request.getRemoteAddr() >> ""

        when:
        service.log(redirectedShortener, request)

        def log = RedirectLog.first()

        then:
        log.clientInformation.browserName == "Chrome 38"
        log.clientInformation.browserVersion == "38.0.1.34"
        log.clientInformation.operatingSystem == "Mac OS X"

    }


    void "log logs the correct geo information for the google dns server as client ip"() {

        given:
        request.getRemoteAddr() >> "8.8.8.8" // Google DNS Server in Mountain View (US)

        when:
        service.log(redirectedShortener, request)

        def log = RedirectLog.first()

        then: "the correct client location information are stored from the geoip service"
        log.clientLocation.countryCode == "US"
        log.clientLocation.countryName == "United States"
        log.clientLocation.city == "Mountain View"

    }


    def createShortener(Map params) {
        def now = new Date()

        def defaultValues = [
                key           : 'abc',
                destinationUrl: 'http://example.com',
                userCreated   : User.first(),
                validFrom     : now,
                validUntil    : now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }
}
