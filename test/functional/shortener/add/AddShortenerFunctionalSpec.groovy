package shortener.add

import geb.spock.GebReportingSpec
import pages.*

class AddShortenerFunctionalSpec extends GebReportingSpec {


    def "a valid shortener will be created and check if the redirection works"() {

        given: "i am at the shortener index page"
        to ShortenerIndexPage

        when: "i click on the create button"
        page.addShortener()

        then: "i am at the creation page"
        at ShortenerCreatePage

        when: "i fill in valid information for the shortner"
        page.createShortener("http://www.google.com", "Dummy User")

        then: "the shortener was created successfully"
        at ShortenerShowPage
        page.isSuccessMessageHere()

        when: "i follow the generated Short-URL"
        page.clickShortUrl()

        then: "i am at the desired destination url"
        driver.currentUrl.contains "google"
    }


    def "an invalid shortener can not be saved and will display error messages"() {

        given: "i am at the shortener index page"
        to ShortenerIndexPage

        when: "i click on the create button"
        page.addShortener()

        then: "i am at the creation page"
        at ShortenerCreatePage

        when: "i fill in valid information for the shortner"
        page.createShortener("noValidUrl", "")

        then: "the shortener was created successfully"
        at ShortenerCreatePage

    }


}