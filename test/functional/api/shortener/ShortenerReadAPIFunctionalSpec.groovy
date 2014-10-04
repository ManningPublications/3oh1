package api.shortener

import geb.spock.GebReportingSpec
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse

import static org.springframework.http.HttpStatus.*

class ShortenerReadAPIFunctionalSpec extends GebReportingSpec {

    private static final String SHORTENERS_API_URL = 'http://localhost:8080/3oh1/api/shorteners'

    RestBuilder client

    def setup() {
        client = new RestBuilder()
    }


    def "GET /api/shorteners/[:id] returns metadata of this shortener"() {

        given:
        def shortenerId = createShortenerFor("http://www.urlViaJsonApi.com")

        when: "the new shortener is requested"
        RestResponse response = client.get(SHORTENERS_API_URL + "/$shortenerId") {
            auth "apiUser", "apiUser"
            accept JSON
        }

        def jsonResponse = response.json

        then: "the request was successful"
        response.statusCode == OK

        and: "the correct shortener was returned"
        jsonResponse.destinationUrl == "http://www.urlViaJsonApi.com"

        and: "different attributes are exposed"
        jsonResponse.containsKey "destinationUrl"
        jsonResponse.containsKey "shortenerKey"
        jsonResponse.containsKey "validFrom"
        jsonResponse.containsKey "validUntil"
        jsonResponse.containsKey "userCreated"

    }




    def "GET /api/shorteners/[:notValidId] returns 404"() {


        when: "a not existing shortener is requested"
        RestResponse response = client.get(SHORTENERS_API_URL + "/aWrongShortenerId") {
            auth "apiUser", "apiUser"
            accept JSON
        }

        then: "the shortener was not found"
        response.statusCode == NOT_FOUND
    }



    def createShortenerFor(String destinationUrl) {

        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth "apiUser", "apiUser"
            accept JSON
            contentType "application/json"

            json destinationUrl: destinationUrl
        }

        def shortenerId = createResponse.headers.getFirst("Location").tokenize("/").last()

        return shortenerId
    }
}