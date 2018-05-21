package api.user

import api.shortener.APIFunctionalSpec
import grails.converters.JSON
import grails.plugins.rest.client.RestResponse

import static org.springframework.http.HttpStatus.*

class UserUpdateAPIFunctionalSpec extends APIFunctionalSpec {

    def setup() {
        defaultUsername = "admin"
        defaultPassword = "admin"
    }

    def "PUT /api/users/[:username]/password with correct password and confirmation returns OK"() {

        setup: 'create a user'
        RestResponse createResponse = client.post(USERS_API_URL) {
            auth defaultUsername, defaultPassword
            accept JSON
            contentType "application/json"

            json {
                username = 'username123'
                password = 'password123'
                confirmPassword = 'password123'
                role = [id: 1]
            }
        }

        when:
        RestResponse response = httpPutJson(
                USERS_API_URL + '/username123' + '/password.json',
                [username       : 'username123',
                 password       : 'password456',
                 confirmPassword: 'password456']
        )

        then:
        response.statusCode == OK

    }

    def "PUT /api/users/[:username]/password without a password returns 422 and error messages"() {

        setup: 'create a user'
        httpPostJson(
                USERS_API_URL, [username: "userWithoutPassword", password: "passord", confirmPassword: "passord"]
        )

        when: 'a HTTP PUT with a null password and confirmPassword is executed to /api/users/[:username]/password'
        RestResponse response = httpPutJson(
                USERS_API_URL + '/userWithoutPassword' + '/password.json', [username: "userWithoutPassword"]
        )

        then: "user was not created and returned a HTTP 422"
        response.statusCode == UNPROCESSABLE_ENTITY

        and: "there is a error message on the password and confirmPassword field"
        def errors = response.json.errors

        errors.find { it.field == "password" }
    }



}