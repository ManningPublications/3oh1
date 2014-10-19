package api.shortener

import grails.converters.JSON
import grails.plugins.rest.client.RestResponse
import static org.springframework.http.HttpStatus.*

class ShortenerCreateAPIFunctionalSpec extends APIFunctionalSpec {


    def "POST /api/shorteners creates a shortener and generates a shortenerKey"() {



        /////////////////////////////////////////////////////////
        // Actual Shortener Creation (HTTP POST)
        /////////////////////////////////////////////////////////

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





        /////////////////////////////////////////////////////////
        // Verify save was successful
        /////////////////////////////////////////////////////////

        when: "the new shortener is requested"
        RestResponse verifyResponse = httpGet(SHORTENERS_API_URL + "/$shortenerId")

        then: "the request was successful"
        verifyResponse.statusCode == OK

        and: "the initially posted destinationUrl was saved"
        verifyResponse.body.destinationUrl == "http://www.urlViaJsonApi.com"

    }



    def "POST /api/shorteners with a given shortenerKey creates a shortener that uses the given shortenerKey"() {


        /////////////////////////////////////////////////////////
        // Actual Shortener Creation (HTTP POST)
        /////////////////////////////////////////////////////////

        when:  "a HTTP POST with a given shortenerKey is executed to /api/shorteners"
        RestResponse createResponse = httpPostJson(
                SHORTENERS_API_URL,
                [destinationUrl: "http://www.urlViaJsonApi.com", shortenerKey: "abc"]
        )


        and: "the response information are extracted"
        def resourceLocation = createResponse.headers.getFirst("Location")
        def shortenerId = resourceLocation.tokenize("/").last()


        then: "shortener was created sucessfully"
        createResponse.statusCode == CREATED




        /////////////////////////////////////////////////////////
        // Verify save was successful
        /////////////////////////////////////////////////////////

        when: "the new shortener is requested"
        RestResponse verifyResponse = httpGet(SHORTENERS_API_URL + "/$shortenerId")

        then: "the request was successful"
        verifyResponse.statusCode == OK

        and: "the initially posted shortenerKey was saved"
        verifyResponse.body.shortenerKey == "abc"


    }


    def "POST /api/shorteners with a given username creates a shortener uses the given username"() {


        /////////////////////////////////////////////////////////
        // Actual Shortener Creation (HTTP POST)
        /////////////////////////////////////////////////////////

        when:  "a HTTP POST with a given username is executed to /api/shorteners"
        RestResponse createResponse = httpPostJson(
                SHORTENERS_API_URL,
                [destinationUrl: "http://www.urlViaJsonApi.com", userCreated: "user"]
        )


        and: "the response information are extracted"
        def resourceLocation = createResponse.headers.getFirst("Location")
        def shortenerId = resourceLocation.tokenize("/").last()


        then: "shortener was created sucessfully"
        createResponse.statusCode == CREATED




        /////////////////////////////////////////////////////////
        // Verify save was successful
        /////////////////////////////////////////////////////////

        when: "the new shortener is requested"
        RestResponse verifyResponse = httpGet(SHORTENERS_API_URL + "/$shortenerId")

        then: "the request was successful"
        verifyResponse.statusCode == OK

        and: "the initially posted username was saved"
        println verifyResponse.json
        verifyResponse.body.userCreated == "user"

    }


    def "POST /api/shorteners with wrong inputs returns 422 and error messages"() {

        when:  "a HTTP POST with an invalid destinationUrl is executed to /api/shorteners"
        RestResponse response = httpPostJson(
                SHORTENERS_API_URL, [destinationUrl: "notAValidDomain"]
        )

        then: "shortener was not created and returned a HTTP 422"
        response.statusCode == UNPROCESSABLE_ENTITY

        and: "there is a error message on the destination url field"
        response.json.errors.find { it.field == "destinationUrl"}

        /*
        the json error object will look like this:
            {
                "errors": [
                    {
                        "object":"io.threeohone.Shortener",
                        "field":"destinationUrl",
                        "rejected-value":"notAValidDomain",
                        "message":"..."
                    }
                ]
            }
         */



    }

}