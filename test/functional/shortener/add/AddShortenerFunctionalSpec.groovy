package shortener.add

import geb.spock.GebReportingSpec
import pages.*
import shortener.ShortenerSpecHelper

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

    def "a shortener with no validUntil value set will be listed in the active list"() {

        given: "two shorteners with just the url and no valid until value"
        createShortenerWithoutExpirationDate("www.endoftheinternet.com")
        createShortenerWithoutExpirationDate("www.endoftheinternet2.com")


        when: "i search for the new shortener"
        page.showValidity("active")
        page.search("endoftheinternet")

        then: "the shortener with no valid until set is displayed"
        page.containsShortener("endoftheinternet.com")

    }

    private createShortenerWithoutExpirationDate(String destinationUrl) {
        at ShortenerIndexPage
        page.addShortener()
        at ShortenerCreatePage
        page.createShortener(destinationUrl)
        to ShortenerIndexPage
    }


}