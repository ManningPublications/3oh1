package io.threeohone

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import io.threeohone.security.Role
import io.threeohone.security.User
import io.threeohone.security.UserRole
import io.threeohone.security.UserRoleController
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

@TestFor(UserRoleController)
@Mock([User, Role, UserRole])
class UserRoleControllerSpec extends Specification {

    def 'that the edit action creates the correct model'() {
        setup:

        def role = new Role(authority: 'testAuth')
        def user = new User(username: 'testUser', password: 'testpass')
        def userRole = new UserRole(user: user, role: role)

        role.save()
        user.save()
        userRole.save()

        when:

        params.id = user.username
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        controller.edit()

        then:
        response.status == 200
        model.roleChangeCommandInstance.username == user.username
    }

    def "edit does not find the user if params.id is an id"() {
        given:
        def user = new User(username: 'noUser')

        when:
        params.id = user.id
        controller.edit()

        then:
        response.status == 404

    }

    def "edit does not find the user if params.id is null"() {
        when:
        params.id = null
        controller.edit()

        then:
        response.status == 404

    }



}
