package io.threeohone

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(RedirectLogController)
@Mock([Shortener])
class RedirectLogControllerSpec extends Specification {


    def "a invalid shortenerId will return a 404"() {

        given:
        params.shortenerId = null

        when:
        controller.show()

        then:
        response.status == 404
    }
}
