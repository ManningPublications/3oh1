package io.threeohone.security

import grails.test.GrailsMock
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
@Mock([User, Role, UserRole])
class UserControllerSpec extends Specification {

    def populateValidParams(params) {
        params['username'] = 'username'
        params['password'] = 'password'
    }

    def "show does not find the user if params.id is an id"() {
        given:
        def user = createUser(username: "notFoundUser")

        when:
        params.id = user.id
        controller.show()

        then:
        response.status == 404

    }

    def "show finds the user if params.id is a valid username"() {
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
        setup:
        def roleMock = new GrailsMock(Role)
        roleMock.demand.static.findByAuthority(1..1) { return new Role(id: 999) }

        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.userCreateCommandInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def user = new User()
        user.validate()
        controller.save(user)

        then: "The create view is rendered again with the correct model"
        model.userInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        user = new User(params)

        controller.save(user)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == "/users/$user.username"
        controller.flash.message != null
        User.count() == 1
    }


    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def user = new User(params)
        controller.edit(user)

        then: 'the editCommand Object is correct'
        model.userEditCommandInstance.username == user.username
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

        def userEditCommand = new UserEditCommand()
        userEditCommand.validate()
        controller.update(userEditCommand)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.userEditCommandInstance == userEditCommand

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        userEditCommand = new UserEditCommand(
                username: params.username,
                password: params.password,
                confirmPassword: params.password)
        controller.update(userEditCommand)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/users/$user.username"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        setup:
        def userRoleMock = new GrailsMock(UserRole)
        userRoleMock.demand.static.findByUser(1..1) {
            return null
        }

        when: "The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        params.id = null
        controller.delete()

        then: "A 404 is returned"
        response.redirectedUrl == '/users'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def user = new User(params).save(flush: true)

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
