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
    protected static final String USERS_API_URL = "${BASE_URL}api/users"

    RestBuilder client

    String defaultUsername = "apiUser"
    String defaultPassword = "apiUser"

    def setup() {
        client = new RestBuilder()
        client.restTemplate.messageConverters.removeAll { it.class.name == 'org.springframework.http.converter.json.GsonHttpMessageConverter' }

    }


    protected def createShortenerFor(String destinationUrl) {

        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth defaultUsername, defaultPassword
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


    protected def createShortenerFor(String destinationUrl, String key) {

        RestResponse createResponse = client.post(SHORTENERS_API_URL) {
            auth defaultUsername, defaultPassword
            accept JSON
            contentType "application/json"

            json destinationUrl: destinationUrl, key: key
        }

        def receivedKey = createResponse.headers.getFirst("Location").tokenize("/").last()

        return receivedKey
    }

    protected JSONElement httpGetJson(String url) {
        return httpGet(url).json
    }

    protected RestResponse httpGet(String url) {
        RestResponse response = client.get(url) {
            auth defaultUsername, defaultPassword
            accept JSON
        }

        return response
    }

    protected RestResponse httpPostJson(String url, Map jsonContent) {

        RestResponse response = client.post(url) {
            auth defaultUsername, defaultPassword
            accept JSON
            contentType "application/json"

            json jsonContent
        }

        return response
    }

    protected RestResponse httpPutJson(String url, Map jsonContent) {

        RestResponse response = client.put(url) {
            auth defaultUsername, defaultPassword
            accept JSON
            contentType "application/json"

            json jsonContent
        }

        return response
    }

}