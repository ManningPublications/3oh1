package user

import geb.spock.GebReportingSpec
import pages.AccessDeniedPage
import pages.LoginPage
import pages.ShortenerCreatePage
import pages.ShortenerIndexPage
import pages.UserCreatePage
import pages.UserEditPage
import pages.UserIndexPage
import pages.UserShowPage


class UserFunctionalSpec extends GebReportingSpec {


    def setup() {
        via UserIndexPage
        at LoginPage
        page.login('admin', 'admin')
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

    def "i can display all shorteners of a user"() {

        given:
        createUser('displayAllShortenersUser', 'password')

        and:
        loginAs('displayAllShortenersUser', 'password')

        when:
        createShortener("http://www.displayAllShortenersUser1.com")
        createShortener("http://www.displayAllShortenersUser2.com")

        and:
        at ShortenerIndexPage
        page.navbar.myShorteners()

        then:
        at ShortenerIndexPage

        and:
        page.numberOfShorteners() == 2

    }



    def 'the password can be changed for a specific user via the ui'() {

        when: 'i click at the username'
        page.showUser('admin')

        then: 'i am at the show page'
        at UserShowPage

        when: 'i change the password'
        page.changePassword()

        then: 'i am at the edit page'
        at UserEditPage

        when: 'i fill in the correct password'
        page.updateUsersPassword('admin')

        then: 'i am at the show page'
        at UserShowPage
        page.isSuccessMessageHere()
    }


    private createShortener(String destinationUrl) {
        to ShortenerIndexPage
        page.addShortener()
        at ShortenerCreatePage
        page.createShortener(destinationUrl)
        to ShortenerIndexPage
    }

    private createUser(String username, String password) {
        at UserIndexPage
        page.addUser()
        at UserCreatePage
        page.createUser(username, password)
        at UserShowPage
        page.isSuccessMessageHere()
    }

    private loginAs(String username, String password) {

        to UserIndexPage
        page.navbar.logout()

        via UserIndexPage
        at LoginPage
        page.login(username, password)
        at UserIndexPage

    }

}