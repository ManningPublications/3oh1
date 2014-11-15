package io.threeohone

import grails.test.mixin.TestFor
import org.grails.plugin.geoip.GeoIpService
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*


@TestFor(RedirectController)
class RedirectControllerSpec extends Specification {


    def shortenerServiceMock
    def redirectLoggingServiceMock

    def setup() {
        shortenerServiceMock = Mock(ShortenerService)
        controller.shortenerService = shortenerServiceMock

        redirectLoggingServiceMock = Mock(RedirectLoggingService)
        controller.redirectLoggingService = redirectLoggingServiceMock

    }

    void 'for a valid shortener entry a redirect should occur to the destination url'() {

        when:
        params.key = 'abc'
        controller.index()

        then:
        response.redirectedUrl == 'http://www.example.com'
        response.status == MOVED_PERMANENTLY.value()

        and: 'the mock was asked once and returned the correct value'
        1 * shortenerServiceMock.findActiveShortenerByKey('abc') >> new Shortener(destinationUrl: 'http://www.example.com')

    }


    void 'a log is created for a valid redirection'() {

        when:
        params.key = 'abc'
        controller.request.addHeader("referer", "http://www.google.com")
        controller.index()

        then:
        response.redirectedUrl == 'http://www.example.com'
        response.status == MOVED_PERMANENTLY.value()

        and: 'the mock was asked once and returned the correct value'
        1 * shortenerServiceMock.findActiveShortenerByKey(_) >> new Shortener(destinationUrl: "http://www.example.com")
        1 * redirectLoggingServiceMock.log(_,request)

    }

    def 'an invalid shortener get a 404 and an error page'() {

        when:
        params.key = 'abc'
        controller.index()

        then:
        response.status == NOT_FOUND.value()
        view == '/notFound'

        and: 'the mock was asked once and returned the correct value'
        1 * shortenerServiceMock.findActiveShortenerByKey('abc') >> null
    }



}
