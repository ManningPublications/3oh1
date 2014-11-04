package api.shortener

import geb.spock.GebReportingSpec
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.codehaus.groovy.grails.web.json.JSONElement
import grails.plugins.rest.client.RestResponse


abstract class APIFunctionalSpec extends GebReportingSpec {


    protected static final String BASE_URL = 'http://localhost:8080/3oh1/'

    protected static final String SHORTENERS_API_URL = "${BASE_URL}api/shorteners"

    RestBuilder client

    def setup() {
        client = new RestBuilder()
    }


    protected def createShortenerFor(String destinationUrl) {

        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth "apiUser", "apiUser"
            accept JSON
            contentType "application/json"

            json destinationUrl: destinationUrl
        }

        def shortenerId = createResponse.headers.getFirst("Location").tokenize("/").last()

        return shortenerId
    }

    protected def createShortenerForWithUser(String destinationUrl, String username, String password) {

        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth username, password
            accept JSON
            contentType "application/json"

            json destinationUrl: destinationUrl
        }

        def shortenerId = createResponse.headers.getFirst("Location").tokenize("/").last()

        return shortenerId
    }


    protected def createShortenerFor(String destinationUrl, String shortenerKey) {

        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth "apiUser", "apiUser"
            accept JSON
            contentType "application/json"

            json destinationUrl: destinationUrl, shortenerKey: shortenerKey
        }

        def shortenerId = createResponse.headers.getFirst("Location").tokenize("/").last()

        return shortenerId
    }

    protected JSONElement httpGetJson(String url) {
        return httpGet(url).json
    }

    protected RestResponse httpGet(String url) {
        RestResponse response = client.get(url) {
            auth "apiUser", "apiUser"
            accept JSON
        }

        return response
    }

    protected RestResponse httpPostJson(String url, Map jsonContent) {

        RestResponse response = client.post(url) {
            auth "apiUser", "apiUser"
            accept JSON
            contentType "application/json"

            json jsonContent
        }

        return response
    }

}