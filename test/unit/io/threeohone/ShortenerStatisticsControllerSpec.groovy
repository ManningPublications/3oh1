package io.threeohone

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import io.threeohone.security.User
import spock.lang.Specification

@TestFor(ShortenerStatisticsController)
@Mock([Shortener, RedirectLog])
class ShortenerStatisticsControllerSpec extends Specification {

    StatisticsService statisticsServiceMock
    private shortener

    def setup() {
        statisticsServiceMock = Mock()
        controller.statisticsService = statisticsServiceMock

        shortener = createShortener()


    }
    void "a show request for a valid shortener returns the total count and the monthly counters of the last year"() {

        given: "there is a shortener which will be requested later"


        statisticsServiceMock.getTotalRedirectsPerMonthBetween(_,_,shortener) >> [
                [month: "12", year: "2013", redirectCounter: 1],
                [month: "11", year: "2013", redirectCounter: 5],
        ]

        when: "the statistics of the shortener will be requested"
        params.shortenerId = shortener.id
        controller.show()
        def json = response.json

        then: "the correct shortenerId is in the json response"
        json.shortenerId == 1

        and: "no redirects have been executed on this shortener"
        json.redirectCounter == 0

        and: "as already mocked, there are two entries for the monthly redirected"
        json.totalNumberOfRedirectsPerMonth.size() == 2

        and: "the first one contains attributes of 12/2013"
        json.totalNumberOfRedirectsPerMonth[0].month == "12"
        json.totalNumberOfRedirectsPerMonth[0].redirectCounter == 1

        and: "the second one contains attributes of 11/2013"
        json.totalNumberOfRedirectsPerMonth[1].month == "11"
        json.totalNumberOfRedirectsPerMonth[1].redirectCounter == 5
    }


    def "a shortener with redirects will display the correct redirectCounter"() {

        given: "there are 10 redirects for the shortener"
        def expectedRedirectCounter = 10

        expectedRedirectCounter.times {
            new RedirectLog(
                    shortener: shortener,
                    referer: "abc",
                    clientIp: "def",
                    userAgent: "ghi"
            ).save(failOnError: true)
        }


        when: "the statistics of the shortener are requested"
        params.shortenerId = this.shortener.id
        controller.show()


        then: "the redirectCounter is returned correctly"
        response.json.redirectCounter == expectedRedirectCounter
    }

    def "a show request with an invalid shortener returns a 404"() {

        when: "an invalid shortener is requested"
        params.shortenerId = null

        controller.show()

        then: "not found is returned"
        response.status == 404
    }


    private Shortener createShortener() {
        new Shortener(
                userCreated: new User(username: "user", password: "user", enabled: true),
                destinationUrl: "http://www.example.com",
                validFrom: new Date()
        ).save(failOnError: true)
    }

}
