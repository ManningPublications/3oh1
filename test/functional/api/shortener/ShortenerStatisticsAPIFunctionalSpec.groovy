package api.shortener


class ShortenerStatisticsAPIFunctionalSpec extends APIFunctionalSpec {



    def "GET /api/shorteners/[:id]/statistics returns statistical information about redirection of the shortener"() {

        given:
        def shortenerId = createShortenerFor("http://www.google.com", "shortenerWithStatistics")

        and: "5 redirections are executed"
        5.times {
            client.get(BASE_URL + "/shortenerWithStatistics")
        }


        when: "the statistics are requested"
        def jsonResponse = httpGetJson(SHORTENERS_API_URL + "/$shortenerId/statistics")


        then: "the stats for the correct shortener are returned"
        jsonResponse.shortenerId == shortenerId.toInteger()

        and: "the correct redirect counter is generated"
        jsonResponse.redirectCounter == 5


    }

}