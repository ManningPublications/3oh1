package io.threeohone

import spock.lang.Specification

class StatisticsServiceIntegrationSpec extends Specification {

    StatisticsService service

    def setup() {
        service = new StatisticsService()
    }



    void "getRedirectCounterGroupedBy returns a list of redirectCounters for the operatingSystem"() {

        given: "one windows and three mac redirects"
        1.times { createRedirectWithOs("Windows") }
        3.times { createRedirectWithOs("Mac OS X") }

        when: "get redirect counters of all shorteners"
        def osRedirectCounters = service.getRedirectCounterGroupedBy(null, 'operatingSystem')

        then: "there are two entries due to two operating systems"
        osRedirectCounters.size() == 2

        and: "the first result is mac with three counts"
        osRedirectCounters[0].label == "Mac OS X"
        osRedirectCounters[0].redirectCounter == 3

        and: "the second result is windows with one count"
        osRedirectCounters[1].label == "Windows"
        osRedirectCounters[1].redirectCounter == 1
    }



    void "getRedirectCounterGroupedBy returns a list of redirectCounters for the browsers"() {

        given: "one windows and three mac redirects"
        1.times { createRedirectWithBrowser("Chrome") }
        3.times { createRedirectWithBrowser("Internet Explorer") }

        when: "get redirect counters of all shorteners"
        def browserRedirectCounters = service.getRedirectCounterGroupedBy(null, 'browserName')

        then: "there are two entries due to two operating systems"
        browserRedirectCounters.size() == 2

        and: "the first result is mac with three counts"
        browserRedirectCounters[0].label == "Internet Explorer"
        browserRedirectCounters[0].redirectCounter == 3

        and: "the second result is windows with one count"
        browserRedirectCounters[1].label == "Chrome"
        browserRedirectCounters[1].redirectCounter == 1
    }


    void "getRedirectCounterGroupedBy with a given shortener only counts for this shortener"() {

        given: "one windows redirect for twitter"
        def twitter = Shortener.findByKey("httpsTwitterCom")
        1.times { createRedirectWithOs("Windows", twitter) }

        and: "one windows redirect for google"
        def google = Shortener.findByKey("httpGoogleCom")
        1.times { createRedirectWithOs("Windows", google) }


        when: "get redirect counters of all shorteners"
        def osRedirectCounters = service.getRedirectCounterGroupedBy(twitter, 'operatingSystem')

        then:"windows with has only one count"
        osRedirectCounters[0].label == "Windows"
        osRedirectCounters[0].redirectCounter == 1
    }




    void "topShorteners returns a list of shorteners with corresponding total number of redirects"() {


        given:
        def twitterComShortener = Shortener.findByKey("httpsTwitterCom")
        10.times { createRedirectFor(twitterComShortener) }

        and:
        def googleComShortener = Shortener.findByKey("httpGoogleCom")
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
        def s = Shortener.findByKey("httpsTwitterCom")
        1.times { createRedirectFor(s, "2015-05-01") }
        2.times { createRedirectFor(s, "2015-04-01") }
        3.times { createRedirectFor(s, "2015-03-01") }


        when:
        def result = service.getTotalRedirectsPerMonthBetween(date("2015-01-01"), date("2015-12-31"))

        then:
        result[0] == [month: "1", year: "2015", redirectCounter: 0]
        result[1] == [month: "2", year: "2015", redirectCounter: 0]
        result[2] == [month: "3", year: "2015", redirectCounter: 3]
        result[3] == [month: "4", year: "2015", redirectCounter: 2]
        result[4] == [month: "5", year: "2015", redirectCounter: 1]

    }



    def "totalRedirectPerMonthBetween with a given shortener returns the total numbers only for this shortener"() {

        given:
        def twitter = Shortener.findByKey("httpsTwitterCom")
        def google = Shortener.findByKey("httpGoogleCom")

        and: "there are two redirects in total in may, but only one for twitter"
        createRedirectFor(twitter, "2015-05-01")
        createRedirectFor(google, "2015-05-01")

        when:
        def result = service.getTotalRedirectsPerMonthBetween(date("2015-01-01"), date("2015-12-31"), twitter)

        then:
        result[4] == [month: "5", year: "2015", redirectCounter: 1]

    }


    void "totalRedirectsPerMonthBetween returns a list with all redirectCounters of 0 when there are no redirects in this time period"() {

        given:
        def twitter = Shortener.findByKey("httpsTwitterCom")
        1.times { createRedirectFor(twitter, "2015-05-01") }

        when:
        def result = service.getTotalRedirectsPerMonthBetween(date("2016-01-01"), date("2016-12-31"))

        then:
        result.each {
            assert it.redirectCounter == 0
        }

        and:
        result.size() == 12

    }

    void "totalRedirectsPerMonthBetween returns an empty list if begin date is greater then end date"() {

        expect:
        service.getTotalRedirectsPerMonthBetween(date("2016-01-01"), date("2015-01-01")) == []

    }

    private RedirectLog createRedirectFor(Shortener shortener, String dateCreated = null) {
        def log = new RedirectLog(
                shortener: shortener,
                referer: "http://www.google.com"
        ).save()

        if (dateCreated) {
            log.dateCreated = date(dateCreated)
            log.save()
        }
    }


    def createRedirectWithOs(String os, Shortener shortener = null) {

        def log = new RedirectLog(
                shortener: shortener ?: Shortener.findByKey("httpsTwitterCom"),
                referer: "http://www.google.com",
                clientInformation: new ClientInformation(
                        browserName: "Chrome 38",
                        browserVersion: "38.0.1.34",
                        operatingSystem: os,
                        mobileBrowser: false
                )

        ).save()
    }


    def createRedirectWithBrowser(String browser, Shortener shortener = null) {

        def log = new RedirectLog(
                shortener: shortener ?: Shortener.findByKey("httpsTwitterCom"),
                referer: "http://www.google.com",
                clientInformation: new ClientInformation(
                        browserName: browser,
                        browserVersion: "38.0.1.34",
                        operatingSystem: "Windows",
                        mobileBrowser: false
                )

        ).save()
    }


    private Date date(String date) {
        Date.parse("yyyy-MM-dd", date)
    }

}
