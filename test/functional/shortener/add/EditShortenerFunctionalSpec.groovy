package shortener.add

import geb.spock.GebReportingSpec
import pages.LoginPage
import pages.ShortenerCreatePage
import pages.ShortenerEditPage
import pages.ShortenerIndexPage
import pages.ShortenerShowPage

class EditShortenerFunctionalSpec extends GebReportingSpec {

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

    def "the destination url of the shortener will be changed and the redirection works for the new url"() {


        given: "a shortener to google.com is already saved"
        createShortenerToGoogle()

        when: "i edit this shortener"
        page.editShortener()

        then: "i am at the edit page of the shortener"
        at ShortenerEditPage

        when: "i update the shortener with a new destination url"
        page.updateShortener("http://www.ebay.com")

        then: "the shortener has been successfully updated"
        at ShortenerShowPage
        page.isSuccessMessageHere()

        when: "the link is followed"
        page.clickShortUrl()


        then: "i am at the desired destination url"
        driver.currentUrl.contains "ebay"
    }




    def "the validFrom value will be set to a future date and after editing no redirect is possible"() {

        given: "a shortener to google.com is already saved"
        createShortenerToGoogle()

        when: "i edit this shortener"
        page.editShortener()

        then: "i am at the edit page of the shortener"
        at ShortenerEditPage

        when: "i update the shortener with a new destination url"
        def tomorrow = new Date() + 1
        page.updateShortener("http://www.ebay.com", tomorrow)

        then: "the update was successfull but is not active anymore"
        at ShortenerShowPage
        !page.isShortenerActive()

    }


    private void createShortenerToGoogle() {
        page.addShortener()
        at ShortenerCreatePage
        page.createShortener("http://www.google.com")
        at ShortenerShowPage
        page.isSuccessMessageHere()
    }

}