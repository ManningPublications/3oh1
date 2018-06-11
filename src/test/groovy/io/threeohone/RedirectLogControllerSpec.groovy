package io.threeohone

import spock.lang.Specification

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
