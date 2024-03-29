package io.threeohone

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import io.threeohone.security.User
import spock.lang.Specification

class StatisticsControllerSpec extends Specification implements ControllerUnitTest<StatisticsController>, DomainUnitTest<Shortener>{
    def setup() {
        mockDomains(RedirectLog)
    }

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

        1 * statisticsService.getTopShorteners() >> RedirectLog.list(max: 10)

    }
}
