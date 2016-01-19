package api.authentication

import geb.spock.GebReportingSpec
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.springframework.http.HttpStatus

class ApiAuthenticationFunctionalSpec extends GebReportingSpec {
    private static final String SHORTENERS_API_URL = 'http://localhost:8080/3oh1/api/shorteners'

    RestBuilder client

    def setup() {
        client = new RestBuilder()
        client.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }
    }


    def "unauthorized access to the api requires a basic auth based credentials"() {

        when:
        RestResponse response = client.get(SHORTENERS_API_URL) {
            accept JSON
        }

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

    }

    def "/api/shorteners with wrong credentials returns unauthorized"() {
        when:
        RestResponse response = client.get(SHORTENERS_API_URL) {
            auth "admin", "wrongPassword"
            accept JSON
        }

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

    }

    def "/api/shorteners with correct credentials returns a successful response"() {
        when:
        RestResponse response = client.get(SHORTENERS_API_URL) {
            auth "admin", "admin"
            accept JSON
        }

        then:
        response.statusCode == HttpStatus.OK

    }

}