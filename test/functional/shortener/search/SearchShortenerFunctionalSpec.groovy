package shortener.search

import geb.spock.GebReportingSpec
import pages.LoginPage
import pages.ShortenerCreatePage
import pages.ShortenerIndexPage
import pages.ShortenerShowPage

class SearchShortenerFunctionalSpec extends GebReportingSpec {



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


    def "search for google.com the active list contains the result"() {

        when: "i search for the domain name"
        page.search("google.com")

        then: "the shortener for google.com is found"
        page.containsShortener("google.com")

    }


    def "search for google.com the expired list contains nothing"() {

        given: "i only see the expired shorteners"
        page.showValidity("expired")

        when: "i search for the domain name"
        page.search("google.com")

        then: "the shortener for google.com is not found"
        !page.containsShortener("google.com")

    }




    def "a shortener can be found by shortenerKey"() {

        when: "i search for the domain name"
        page.search("httpSpecViaHttps")

        then: "the shortener for https://www.ietf.org/rfc/rfc2616.txt is not found"
        page.containsShortener("ietf.org/rfc/rfc2616.txt")

    }


    def "a search for a domain name finds all shorteners with this domain and different paths"() {
        given: "i create two shorteners to different articles at wikipedia"
        createShortener("http://en.wikipedia.org/wiki/IPv4")
        createShortener("http://en.wikipedia.org/wiki/IPv6")

        when: "i search for the domain name"
        page.search("wikipedia.org")

        then: "both articles are found"
        page.numberOfShorteners() == 2

    }

    def "a search for a domain name finds all shorteners with this domain and different subdomains"() {
        given: "i create two shorteners to different articles at wikipedia"
        createShortener("http://de.yahoo.com")
        createShortener("http://fr.yahoo.com")

        when: "i search for the domain name"
        page.search("yahoo.com")

        then: "both yahoo sites are found"
        page.numberOfShorteners() == 2

    }


    def "a search for a domain name finds http and https links"() {
        given: "i create two shorteners to different articles at wikipedia"
        createShortener("http://netflix.com")
        createShortener("https://netflix.com")

        when: "i search for the domain name"
        page.search("netflix.com")

        then: "both yahoo sites are found"
        page.numberOfShorteners() == 2

    }


    private createShortener(String destinationUrl) {
        at ShortenerIndexPage
        page.addShortener()
        at ShortenerCreatePage
        page.createShortener(destinationUrl)
        to ShortenerIndexPage
    }
}