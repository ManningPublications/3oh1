package user

import geb.spock.GebReportingSpec
import pages.*

class UserCrudAccessFunctionalSpec extends GebReportingSpec {


    def "an user is not allowed to access the users administration"() {

        when: "logging in as user"
        via ShortenerIndexPage
        at LoginPage
        page.login('user', 'user')

        then: "the menu entry is not available"
        at ShortenerIndexPage
        !page.navbar.isUsersMenuAvailable()

        when: "one tries to directly go to the user crud page"
        via UserIndexPage

        then: "a access denied error occured"
        at AccessDeniedPage

    }



}