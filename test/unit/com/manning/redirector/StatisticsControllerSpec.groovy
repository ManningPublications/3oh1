package com.manning.redirector

import com.manning.Shortener
import com.manning.security.User
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(StatisticsController)
@Mock([Shortener, RedirectLog, User])
class StatisticsControllerSpec extends Specification {

    void "index returns the last 5 redirects"() {

        given:
        def shortener = new Shortener(
                userCreated: new User(username: "user", password: "user", enabled: true),
                destinationUrl: "http://www.google.com",
                shortenerKey: "abc",
                validFrom: new Date()
        ).save(failOnError: true)
        6.times {
            new RedirectLog(
                    shortener: shortener,
                    clientIp: "127.0.0.1",
                    userAgent: "MAC OS X"
            ).save(failOnError: true)
        }

        when:
        controller.index()

        then:
        model.redirectLogInstanceList.size() == 5

    }
}
