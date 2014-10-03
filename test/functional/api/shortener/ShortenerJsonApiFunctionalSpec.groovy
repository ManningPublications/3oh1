package api.shortener

import geb.spock.GebReportingSpec
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import static org.springframework.http.HttpStatus.*

class ShortenerJsonApiFunctionalSpec extends GebReportingSpec {

    private static final String SHORTENERS_API_URL = 'http://localhost:8080/3oh1/api/shorteners'

    RestBuilder client

    def setup() {
        client = new RestBuilder()
    }


    def "POST /shorteners creates a shortener and generates a shortenerKey"() {

        when: "a HTTP POST with JSON content is executed to /api/shorteners"
        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth "apiUser", "apiUser"
            accept JSON
            contentType "application/json"

            json destinationUrl: "http://www.urlViaJsonApi.com"
        }


        and: "the response information are extracted"
        def resourceLocation = createResponse.headers.getFirst("Location")
        def shortenerId = resourceLocation.tokenize("/").last()


        then: "shortener was created sucessfully"
        createResponse.statusCode == CREATED

        when: "the new shortener is requested"
        RestResponse verifyResponse = client.get(SHORTENERS_API_URL + "/$shortenerId") {
            auth "apiUser", "apiUser"
            accept JSON
        }

        then: "the request was successful"
        verifyResponse.statusCode == OK

        and: "the initially posted destinationUrl was saved"
        verifyResponse.body.destinationUrl == "http://www.urlViaJsonApi.com"

    }



    def "POST /shorteners with a given shortenerKey creates a shortener uses the given shortenerKey"() {

        when:  "a HTTP POST with a given shortenerKey is executed to /api/shorteners"
        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth "apiUser", "apiUser"
            accept JSON
            contentType "application/json"

            json destinationUrl: "http://www.urlViaJsonApi.com", shortenerKey: "abc"
        }


        and: "the response information are extracted"
        def resourceLocation = createResponse.headers.getFirst("Location")
        def shortenerId = resourceLocation.tokenize("/").last()


        then: "shortener was created sucessfully"
        createResponse.statusCode == CREATED

        when: "the new shortener is requested"
        RestResponse verifyResponse = client.get(SHORTENERS_API_URL + "/$shortenerId") {
            auth "apiUser", "apiUser"
            accept JSON
        }

        then: "the request was successful"
        verifyResponse.statusCode == OK

        and: "the initially posted destinationUrl was saved"
        verifyResponse.body.shortenerKey == "abc"


    }

}