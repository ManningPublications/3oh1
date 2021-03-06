package io.threeohone

import io.threeohone.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(StatisticsController)
@Mock([Shortener, RedirectLog, User])
class StatisticsControllerSpec extends Specification {

    void "index returns the last 10 redirects"() {

        given:
        def statisticsService = Mock(StatisticsService)
        controller.statisticsService = statisticsService

        def shortener = new Shortener(
                userCreated: new User(username: "user", password: "user", enabled: true),
                destinationUrl: "http://www.google.com",
                key: "abc",
                validFrom: new Date()
        ).save(failOnError: true)

        20.times {
            new RedirectLog(shortener: shortener).save(failOnError: true)
        }

        when:
        controller.index()

        then:
        model.redirectLogInstanceList.size() == 10

        1 * statisticsService.getTopShorteners() >> RedirectLog.list(max:10)

    }
}
