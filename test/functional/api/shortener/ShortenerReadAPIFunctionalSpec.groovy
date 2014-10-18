package api.shortener

import grails.plugins.rest.client.RestResponse

import static org.springframework.http.HttpStatus.*

class ShortenerReadAPIFunctionalSpec extends APIFunctionalSpec {


    def "GET /api/shorteners/[:id] returns metadata of this shortener"() {

        given:
        def shortenerId = createShortenerFor("http://www.urlViaJsonApi.com")

        when: "the new shortener is requested"
        RestResponse response = httpGet(SHORTENERS_API_URL + "/$shortenerId")
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

        and: "the correct user is returned"
        jsonResponse.containsKey "userCreated"
        jsonResponse.userCreated == "apiUser"

        and: "class and id attribute are hidden"
        !jsonResponse.containsKey("class")
        !jsonResponse.containsKey("id")

    }


    def "GET /api/shorteners/[:notValidId] returns 404"() {

        when: "a not existing shortener is requested"
        RestResponse response = httpGet(SHORTENERS_API_URL + "/aWrongShortenerId")

        then: "the shortener was not found"
        response.statusCode == NOT_FOUND
    }


}