package io.threeohone.security

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.datastore.mapping.core.connections.ConnectionSource
import org.grails.datastore.mapping.simple.SimpleMapDatastore
import spock.lang.AutoCleanup
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

@Ignore
class UserControllerSpec extends Specification implements ControllerUnitTest<UserController>, DomainUnitTest<User> {

    @Shared @AutoCleanup SimpleMapDatastore dataStore = new SimpleMapDatastore([ConnectionSource.DEFAULT, "userLookup"], User)

    def setup() {
        mockDomains(UserRole, Role)
        controller.springSecurityService = Mock(SpringSecurityService)
    }

    def populateValidParams(params) {
        params['username'] = 'username'
        params['password'] = 'password'
    }

    void "show does not find the user if params.id is an id"() {
        given:
        def user = createUser(username: "notFoundUser")

        when:
        params.id = user.id
        controller.show()

        then:
        response.status == 404

    }

    void "show finds the user if params.id is a valid username"() {
        given:
        createUser(username: "foundUser")

        when:
        params.id = "foundUser"
        controller.show()

        then:
        response.status == 200

    }

    private User createUser(params) {
        params.username = params.username ?: "defaultUser"
        params.password = params.password ?: "defaultPassword"
        new User(params).save(failOnError: true, flush: true)
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.userInstanceList
        model.userInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {

        given: "there is a user role in the database that the new user can be assigned to"
        User user = new User(username: 'auser', password: 'apass')

        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.userCreateCommandInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        given: "there is a user role in the database that the new user can be assigned to"
        new Role(authority: "ROLE_USER").save(failOnError: true, flush: true)

        when: "The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def user = new UserCreateCommand()
        user.validate()
        controller.save(user)

        then: "The create view is rendered again with the correct model"
        model.userCreateCommandInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        user = new UserCreateCommand(params)

        controller.save(user)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == "/users/$user.username"
        controller.flash.message != null
        User.count() == 1
    }


    void "edit does not find the user if params.id is not a valid username"() {
        when:
        params.id = null
        controller.edit()

        then:
        response.status == 404

        when:
        params.id = "noValidUsername"
        controller.edit()

        then:
        response.status == 404

    }

    void 'edit creates a correct userChangeCommand if a valid username is given'() {

        setup:
        def role = new Role(authority: 'testAuth').save(failOnError: true, flush: true)
        def user = new User(username: 'testUser', password: 'testpass').save(failOnError: true, flush: true)
        new UserRole(user: user, role: role).save(failOnError: true, flush: true)

        params.id = user.username

        when:
        controller.edit()

        then:
        response.status == 200
        model.userChangeCommandInstance.username == user.username
    }


    void "Test the update action performs an update on a valid domain instance"() {
        setup:
        populateValidParams(params)
        def user = new User(
                username: params.username,
                password: params.password)

        user.save(failOnError: true, flush: true)

        when: "Update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/users'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()

        def userChangeCommand = new UserChangeCommand()
        userChangeCommand.validate()
        controller.update(userChangeCommand)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.userChangeCommandInstance == userChangeCommand

    }

    void "the update action updates a user if valid form inputs are given"() {
        given:
        def role = new Role(authority: 'testAuth').save(failOnError: true, flush: true)
        def user = new User(username: 'testUser', password: 'testpass').save(failOnError: true, flush: true)
        new UserRole(user: user, role: role).save(failOnError: true, flush: true)


        controller.springSecurityService.currentUser >> user

        and: "the form values are setup"
        def userChangeCommand = new UserChangeCommand(
                username: user.username,
                role: role
        )
        userChangeCommand.validate()

        and: "the request is marked as a form submit"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        when: "the form is send to the controller"
        controller.update(userChangeCommand)

        then: "A redirect is issues to the show action"
        response.status == 302
        response.redirectedUrl == "/users/$user.username"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {

        when: "The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        params.id = null
        controller.delete()

        then: "A 404 is returned"
        response.redirectedUrl == '/users'
        flash.message != null

        when: "a user with a role is created"
        response.reset()
        populateValidParams(params)
        def user = new User(params).save(flush: true)
        def role = new Role(authority: "ROLE_USER").save(failOnError: true, flush: true)
        UserRole.create(user, role).save flush: true

        then: "It exists"
        User.count() == 1

        when: "The domain instance is passed to the delete action"
        params.id = user.username
        controller.delete()

        then: "The instance is deleted"
        User.count() == 0
        response.redirectedUrl == '/users'
        flash.message != null
    }
}
