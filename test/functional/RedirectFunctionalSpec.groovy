import geb.spock.GebReportingSpec

class RedirectFunctionalSpec extends GebReportingSpec {
    private static final String BASE_URL = 'http://localhost:8080/redirector/'


    def "a http redirect works correctly (google)"() {
        when:
        go BASE_URL + "httpGoogleCom"

        then:
        driver.currentUrl.contains "google"

    }

    def "a http redirect works correctly (w3c http spec)"() {
        when:
        go BASE_URL + "httpSpec"

        then:
        driver.currentUrl == "http://www.w3.org/Protocols/rfc2616/rfc2616.html"

    }


    def "a https redirect works correctly (w3c http spec)"() {
        when:
        go BASE_URL + "httpSpecViaHttps"

        then:
        driver.currentUrl == "https://www.ietf.org/rfc/rfc2616.txt"

    }

    def "a https redirect works correctly (twitter)"() {
        when:
        go BASE_URL + "httpsTwitterCom"

        then:
        driver.currentUrl.contains "https://"
        driver.currentUrl.contains "twitter.com"

    }

}