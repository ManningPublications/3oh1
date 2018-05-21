package api.user

import api.shortener.APIFunctionalSpec
import grails.converters.JSON
import grails.plugins.rest.client.RestResponse

import static org.springframework.http.HttpStatus.*

class UserCreateAPIFunctionalSpec extends APIFunctionalSpec {

    def setup() {
        defaultUsername = "admin"
        defaultPassword = "admin"
    }

    def "POST /api/users creates a user"() {

        /////////////////////////////////////////////////////////
        // Actual User Creation (HTTP POST)
        /////////////////////////////////////////////////////////

        when: "a HTTP POST with JSON content is executed to /api/users"
        RestResponse createResponse = client.post(USERS_API_URL) {
            auth defaultUsername, defaultPassword
            accept JSON
            contentType "application/json"

            json {
                username = 'username'
                password = 'password'
                confirmPassword = 'password'
                role = [id: 1]
            }
        }

        and: "the response information are extracted"
        def resourceLocation = createResponse.headers.getFirst("Location")
        def userName = resourceLocation.tokenize("/").last()


        then: "User was created sucessfully"
        createResponse.statusCode == CREATED

        /////////////////////////////////////////////////////////
        // Verify save was successful
        /////////////////////////////////////////////////////////

        when: "the new shortener is requested"
        RestResponse verifyResponse = httpGet(USERS_API_URL + "/$userName")

        then: "the request was successful"
        verifyResponse.statusCode == OK

        and: "the initially posted username was saved"
        verifyResponse.body.username == "username"

    }

    def "POST /api/users without a password returns 422 and error messages"() {

        when: "a HTTP POST with a null password and confirmPassword is executed to /api/users"
        RestResponse response = httpPostJson(
                USERS_API_URL, [username: "userWithoutPassword"]
        )

        then: "user was not created and returned a HTTP 422"
        response.statusCode == UNPROCESSABLE_ENTITY

        and: "there is a error message on the password and confirmPassword field"
        def errors = response.json.errors

        errors.find { it.field == "password" }
        errors.find { it.field == "confirmPassword" }

        /*  the json error object will look like this:

          {
              "errors": [{
                             "object": "io.threeohone.security.UserCreateCommand", "field": "password",
                             "rejected-value": null, "message": "password cannot be null"
                         }, {
                             "object": "io.threeohone.security.UserCreateCommand", "field": "confirmPassword",
                             "rejected-value": null, "message": "confirmPassword cannot be null"
                         }]
          }

        */


    }

    def "POST /api/users with a difference in password and confirmPassword returns 422 and error messages"() {

        when: "a HTTP POST with different password and confirmPassword is executed to /api/users"
        RestResponse response = httpPostJson(
                USERS_API_URL, [username: 'userWithWrongPasswordConfirmation', password: 'password', confirmPassword: 'differentPassword']
        )

        then: "user was not created and returned a HTTP 422"
        response.statusCode == UNPROCESSABLE_ENTITY

        and: "there is a error message on the password and confirmPassword field"
        def errors = response.json.errors

        errors.find { it.field == "password" }

        /*

        {
            the json error object will look like this:

                    "errors": [{
                           "object": "io.threeohone.security.UserCreateCommand", "field": "password",
                           "rejected-value": "password", "message":
                           "Password does not match the confirm password."
                       }]
        }

        */

    }

}