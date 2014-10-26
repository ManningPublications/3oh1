package statistics

import geb.download.DownloadException
import geb.spock.GebReportingSpec
import io.threeohone.Shortener
import pages.LoginPage
import pages.ShortenerCreatePage
import pages.ShortenerIndexPage
import pages.ShortenerShowPage
import pages.StatisticsPage

class StatisticsFunctionalSpec extends GebReportingSpec {

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


    def "when a redirect is executed, the redirection will be shown in the statistics page afterwards"() {

        given: "there is a shortener to grails.org"
        createShortener("http://www.grails.org")
        at ShortenerShowPage

        when: "execute the redirect"
        page.clickShortUrl()

        and: "go to the statistics page"
        to ShortenerIndexPage
        page.navbar.statistics()

        then: "the redirect grails.org is found"
        at StatisticsPage
        page.isLastRedirect("grails.org")

    }

    private createShortener(String destinationUrl) {
        at ShortenerIndexPage
        page.addShortener()
        at ShortenerCreatePage
        page.createShortener(destinationUrl)
    }
}