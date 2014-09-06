package shortener.add

import geb.spock.GebReportingSpec
import pages.*
import spock.lang.Stepwise

@Stepwise
class AddShortenerFunctionalSpec extends GebReportingSpec {


    def "when i click on the create button at the index page"() {
        when:
        to ShortenerIndexPage

        page.addShortener()

        then:
        at ShortenerCreatePage

    }

    def "and i fill in valid information for the shortner"() {
        given:
        at ShortenerCreatePage

        when:
        page.setFormValues("http://www.google.com", "Dummy User")

        and:
        page.save()

        then:
        at ShortenerShowPage
        page.isSuccessMessageHere()

    }

    def "then i'm at the show page of the new shortener and can follow the generated Short-URL"() {

        when:
        page.clickShortUrl()

        then:
        driver.currentUrl.contains "google"

    }



}