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
        page.logout()
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


}