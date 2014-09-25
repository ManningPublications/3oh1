package io.threeohone

import spock.lang.Specification

class StatisticsServiceIntegrationSpec extends Specification {

    StatisticsService service

    def setup() {
        service = new StatisticsService()
    }


    void "getTopShorteners returns a list of shorteners with corresponding total number of redirects"() {


        given:
        def twitterComShortener = Shortener.findByShortenerKey("httpsTwitterCom")
        10.times { createRedirectFor(twitterComShortener) }

        and:
        def googleComShortener = Shortener.findByShortenerKey("httpGoogleCom")
        2.times { createRedirectFor(googleComShortener) }



        when:
        def result = service.getTopShorteners()

        then:
        result[0].shortener == twitterComShortener
        result[0].redirectCounter == 10

        and:
        result[1].shortener == googleComShortener
        result[1].redirectCounter == 2

    }
    void "getTopShorteners returns an empty list of no redirects have happend"() {

        expect: "if there are no entries in the redirectLog table, there is no result"
        service.getTopShorteners() == []

    }

    private RedirectLog createRedirectFor(Shortener shortener) {
        new RedirectLog(
                shortener: shortener,
                userAgent: "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                clientIp: "192.168.0.24",
                referer: "http://www.google.com"
        ).save()
    }
}
