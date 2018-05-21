package io.threeohone.security

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PasswordController)
@Mock([User, UserRole, Role])
class PasswordControllerSpec extends Specification {

    def setup() {
        controller.springSecurityService = Mock(SpringSecurityService)
    }

    void "edit method renders the form with the correct user"() {

        given:
        def expectedUser = new User(username: "user", password: "password").save(failOnError: true, flush: true)
        params.userId = expectedUser.username

        and: "the same user is logged in"
        controller.springSecurityService.getCurrentUser() >> expectedUser

        when:
        controller.edit()

        then:
        model.passwordChangeCommandInstance.username == expectedUser.username

    }

    def "edit returns 404 if the username is not given"() {

        given:
        params.userId = null

        and: "the admin is logged in"
        controller.springSecurityService.getCurrentUser() >> createAdmin()

        when:
        controller.edit()

        then:
        response.status == 404
    }

    def "edit form will not be shown if a not-admin will try to change another users password"() {

        given:
        def loggedInUser = new User(username: "loggedInUser", password: "password").save(failOnError: true, flush: true)
        def expectedUser = new User(username: "userWithPasswordChange", password: "oldPassword").save(failOnError: true, flush: true)

        and:
        params.userId = expectedUser.username


        and: "not the expected user is logged in"
        controller.springSecurityService.getCurrentUser() >> loggedInUser

        when:
        controller.edit()

        then:
        response.status == 302

        and:
        flash.error == 'springSecurity.denied.message'
    }

    def "execute update as a admin with the correct password values will change the password of a user"() {

        given: "there is a user to change"
        def expectedUser = new User(username: "userWithPasswordChange", password: "oldPassword").save(failOnError: true, flush: true)

        and: "a logged in administrator will be emulated"
        def admin = createAdmin()
        controller.springSecurityService.loadCurrentUser() >> admin
        controller.springSecurityService.getCurrentUser() >> admin

        and: "the form data is set up"
        def passwordChangeCommand = new PasswordChangeCommand(
            username: expectedUser.username,
            password: "newPassword",
            confirmPassword: "newPassword"
        )


        and: "the form is set up correctly"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        when:
        controller.update(passwordChangeCommand)


        then: "the password was updated"
        expectedUser.password == "newPassword"

        and: "a redirect happens to the shortener list"
        response.status == 302
        response.redirectedUrl == "/shorteners"

    }

    def "execute update as a admin with incorrect password values will not change the password of a user"() {

        given: "there is a user to change"
        def expectedUser = new User(username: "userWithPasswordChange", password: "oldPassword").save(failOnError: true, flush: true)

        and: "a logged in administrator will be emulated"
        def admin = createAdmin()
        controller.springSecurityService.loadCurrentUser() >> admin
        controller.springSecurityService.getCurrentUser() >> admin

        and: "the form data is set up"
        def passwordChangeCommand = new PasswordChangeCommand(
            username: expectedUser.username,
            password: "newPassword",
            confirmPassword: "otherPassword"
        )

        and: "validate has to called manually, because in in test mode validate() on the parsed command object is not called by grails"
        passwordChangeCommand.validate()

        and: "the form is set up correctly"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        when:
        controller.update(passwordChangeCommand)


        then: "the password was updated"
        expectedUser.password == "oldPassword"

        and: "a redirect happens to the shortener list"
        view == "edit"

        and: "the returned model contains errors to show for in the form"
        model.passwordChangeCommandInstance.hasErrors()

    }

    def "execute update as a user is possible for yourself with correct values"() {

        given: "there is a user to change"
        def expectedUser = new User(username: "userWithPasswordChange", password: "oldPassword").save(failOnError: true, flush: true)

        and: "the same user is logged in"
        controller.springSecurityService.loadCurrentUser() >> expectedUser
        controller.springSecurityService.getCurrentUser() >> expectedUser

        and: "the form data is set up"
        def passwordChangeCommand = new PasswordChangeCommand(
            username: expectedUser.username,
            password: "newPassword",
            confirmPassword: "newPassword"
        )


        and: "the form is set up correctly"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        when:
        controller.update(passwordChangeCommand)


        then: "the password was updated"
        expectedUser.password == "newPassword"

        and: "a redirect happens to the shortener list"
        response.status == 302
        response.redirectedUrl == "/shorteners"

    }

    def "execute update as a user is not possible for other users"() {

        given: "there is a user to change"
        def loggedInUser = new User(username: "loggedInUser", password: "password").save(failOnError: true, flush: true)
        def expectedUser = new User(username: "userWithPasswordChange", password: "oldPassword").save(failOnError: true, flush: true)

        and: "the same user is logged in"
        controller.springSecurityService.loadCurrentUser() >> loggedInUser
        controller.springSecurityService.getCurrentUser() >> loggedInUser

        and: "the form data is set up"
        def passwordChangeCommand = new PasswordChangeCommand(
            username: expectedUser.username,
            password: "newPassword",
            confirmPassword: "newPassword"
        )


        and: "the form is set up correctly"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        when:
        controller.update(passwordChangeCommand)


        then: "the password was not updated"
        expectedUser.password == "oldPassword"

        and: "access denied message is shown"
        flash.error == 'springSecurity.denied.message'


    }

    private User createAdmin() {
        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true, failOnError: true)
        def admin = new User(username: "admin", password: "admin").save(flush: true, failOnError: true)
        new UserRole(user: admin, role: adminRole).save(flush: true, failOnError: true)

        return admin

    }
}
