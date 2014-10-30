package user

import geb.spock.GebReportingSpec
import pages.LoginPage
import pages.UserCreatePage
import pages.UserEditPage
import pages.UserIndexPage
import pages.UserShowPage

class UserFunctionalSpec extends GebReportingSpec {


    def setup() {
        via UserIndexPage
        at LoginPage
        page.login('user', 'user')
        at UserIndexPage
    }

    def cleanup() {
        to UserIndexPage
        page.navbar.logout()
    }

    def 'a valid user will be created and deleted'() {

        when: 'i click on the create button'
        page.addUser()

        then: 'i am at the creation page'
        at UserCreatePage

        when: 'i fill in valid information for the user'
        page.createUser('testuser', 'password')

        then: 'the user was created successfully'
        at UserShowPage
        page.isSuccessMessageHere()

        when: 'i click on delete'
        page.deleteUser()

        then: 'the user was deleted successfully'
        at UserIndexPage
        page.isSuccessMessageHere()

    }

    def 'change password'() {

        when: 'i click an the username'
        page.editUser('user')

        then: 'i am at the show page'
        at UserShowPage

        when: 'i change the password'
        page.changePassword()

        then: 'i am at the edit page'
        at UserEditPage

        when: 'i fill in the correct password'
        page.updateUsersPassword('user')

        then: 'i am at the show page'
        at UserShowPage
        page.isSuccessMessageHere()
    }



}