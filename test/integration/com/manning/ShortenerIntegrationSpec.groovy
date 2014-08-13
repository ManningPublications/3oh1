package com.manning

import spock.lang.*

class ShortenerIntegrationSpec extends Specification {


    void "a shortened url can only occur once in the db"() {

        setup: "there is a shortener with a valid url"
        def uniqueShortenedUrl = "http://mng.bz/123"
        def shortenerWithValidUrl = new Shortener(
                destinationUrl: "http://example.com",
                shortenerKey: uniqueShortenedUrl,
                userCreated: "Dummy User",
                validFrom: new Date(),
                validUntil: new Date() + 1
        )

        shortenerWithValidUrl.save(failOnError: true, flush: true)

        and: "there is another shortener with the same shortened url"
        def shortenerWithDuplicatedUrl = new Shortener(
                destinationUrl: "http://example2.com",
                shortenerKey: uniqueShortenedUrl,
                userCreated: "Dummy User2",
                validFrom: new Date(),
                validUntil: new Date() + 1
        )

        when: "try to save the second shortener"
        def saveWasSuccessful = shortenerWithDuplicatedUrl.save(flush: true)

        then:
        !saveWasSuccessful

        and: "unique was the error that leads to a failing save"
        def errorCodesForShortenedUrl = shortenerWithDuplicatedUrl.errors.getFieldError("shortenerKey").codes
        errorCodesForShortenedUrl.contains("unique")

    }


}
