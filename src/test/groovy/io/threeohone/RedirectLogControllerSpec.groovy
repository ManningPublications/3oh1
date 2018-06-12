package io.threeohone

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class RedirectLogControllerSpec extends Specification implements ControllerUnitTest<RedirectLogController>, DomainUnitTest<Shortener>{

    void "a invalid shortenerId will return a 404"() {

        given:
        params.shortenerId = null

        when:
        controller.show()

        then:
        response.status == 404
    }
}
