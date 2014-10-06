package shortener.add

import geb.spock.GebReportingSpec
import pages.*

class AddShortenerFunctionalSpec extends GebReportingSpec {

    def setup() {
        via ShortenerIndexPage
        at LoginPage
        page.login("user", "user")
        at ShortenerIndexPage
    }

    def cleanup() {
        to ShortenerIndexPage
        page.navbar.logout()
    }

    def "a valid shortener will be created and check if the redirection works"() {

        when: "i click on the create button"
        page.addShortener()

        then: "i am at the creation page"
        at ShortenerCreatePage

        when: "i fill in valid information for the shortner"
        page.createShortener("http://www.google.com")

        then: "the shortener was created successfully"
        at ShortenerShowPage
        page.isSuccessMessageHere()

        when: "i follow the generated Short-URL"
        page.clickShortUrl()

        then: "i am at the desired destination url"
        driver.currentUrl.contains "google"

    }

    def "an invalid shortener can not be saved and will display error messages"() {


        when: "i click on the create button"
        page.addShortener()

        then: "i am at the creation page"
        at ShortenerCreatePage

        when: "i fill in valid information for the shortner"
        page.createShortener("noValidUrl")

        then: "the shortener was created successfully"
        at ShortenerCreatePage


    }

    // shows the bugfix for #23 (https://github.com/ManningPublications/3oh1/issues/23)
    def "a shortener with no validUntil value set will be listed in the active list"() {

        when: "i click on the create button"
        page.addShortener()

        then: "i am at the creation page"
        at ShortenerCreatePage

        when: "i fill in valid information for the shortner"
        page.createShortener("http://www.endoftheinternet.com/")

        then: "the shortener was created successfully"
        at ShortenerShowPage
        page.isSuccessMessageHere()

        and: "i go to the shortener list"
        to ShortenerIndexPage

        when: "i click on the create button"
        page.addShortener()

        //find 1 shortener redirect to show page, therefore create a second shortener
        then: "i am at the creation page"
        at ShortenerCreatePage

        when: "i fill in valid information for the shortner"
        page.createShortener("http://www.endoftheinternet2.com/")

        then: "the shortener was created successfully"
        at ShortenerShowPage
        page.isSuccessMessageHere()

        when: "i go to the shortener list"
        to ShortenerIndexPage

        and: "i search for the new shortener"
        page.showValidity("active")
        page.search("endoftheinternet")

        then: "the shortener for google.com is not found"
        page.containsShortener("endoftheinternet.com")

    }


}