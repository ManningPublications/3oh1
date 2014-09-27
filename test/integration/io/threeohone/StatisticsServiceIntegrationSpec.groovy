package io.threeohone

import spock.lang.Specification

class StatisticsServiceIntegrationSpec extends Specification {

    StatisticsService service

    def setup() {
        service = new StatisticsService()
    }


    void "topShorteners returns a list of shorteners with corresponding total number of redirects"() {


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


    void "topShorteners returns an empty list of no redirects have happend"() {

        expect: "if there are no entries in the redirectLog table, there is no result"
        service.getTopShorteners() == []

    }


    void "totalRedirectsPerMonthBetween returns a list of months with corresponding total number of redirects"() {

        given:
        def s = Shortener.findByShortenerKey("httpsTwitterCom")
        1.times { createRedirectFor(s, "2015-05-01") }
        2.times { createRedirectFor(s, "2015-04-01") }
        3.times { createRedirectFor(s, "2015-03-01") }


        when:
        def result = service.getTotalRedirectsPerMonthBetween(date("2015-01-01"), date("2015-12-31"))

        then:
        result[0] == [month: "3/2015", redirectCounter: 3]
        result[1] == [month: "4/2015", redirectCounter: 2]
        result[2] == [month: "5/2015", redirectCounter: 1]

    }

    void "totalRedirectsPerMonthBetween returns an empty list of months with corresponding total number of redirects"() {

        given:
        def s = Shortener.findByShortenerKey("httpsTwitterCom")
        1.times { createRedirectFor(s, "2015-05-01") }

        when:
        def result = service.getTotalRedirectsPerMonthBetween(date("2016-01-01"), date("2016-12-31"))

        then:
        result == []

    }

    void "totalRedirectsPerMonthBetween returns an empty list if begin date is greater then end date"() {

        expect:
        service.getTotalRedirectsPerMonthBetween(date("2016-01-01"), date("2015-01-01")) == []

    }

    private RedirectLog createRedirectFor(Shortener shortener, String dateCreated = null) {
        def log = new RedirectLog(
                shortener: shortener,
                userAgent: "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                clientIp: "192.168.0.24",
                referer: "http://www.google.com"
        ).save()

        if (dateCreated) {
            log.dateCreated = date(dateCreated)
            log.save()
        }
    }

    private Date date(String date) {
        Date.parse("yyyy-MM-dd", date)
    }

}
