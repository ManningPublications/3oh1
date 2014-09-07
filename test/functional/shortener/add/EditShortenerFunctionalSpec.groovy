package shortener.add

import geb.spock.GebReportingSpec
import pages.ShortenerCreatePage
import pages.ShortenerEditPage
import pages.ShortenerIndexPage
import pages.ShortenerShowPage

class EditShortenerFunctionalSpec extends GebReportingSpec {


    def "the destination url of the shortener will be changed and the redirection works for the new url"() {

        given: "a shortener to google.com is already saved"
        to ShortenerIndexPage
        page.addShortener()
        at ShortenerCreatePage
        page.createShortener("http://www.google.com", "Dummy User")
        at ShortenerShowPage
        page.isSuccessMessageHere()

        when: "i edit this shortener"
        page.editShortener()

        then: "i am at the edit page of the shortener"
        at ShortenerEditPage

        when: "i update the shortener with a new destination url"
        page.updateShortener("http://www.ebay.com", "Dummy User")

        then:
        at ShortenerShowPage
        page.isSuccessMessageHere()
        page.clickShortUrl()


        then: "i am at the desired destination url"
        driver.currentUrl.contains "ebay"
    }

}