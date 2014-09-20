package io.threeohone

import io.threeohone.security.User
import spock.lang.*

class ShortenerServiceFindIntegrationSpec extends Specification {


    private service

    def setup() {
        service  = new ShortenerService()
    }

    void 'a valid shortenerKey will find the correct shortener'() {

        setup:

        def expectedDestinationUrl = 'http://example.com'
        createShortener(shortenerKey: 'abc', destinationUrl: expectedDestinationUrl)

        when:
        def actualShortener = this.service.findActiveShortenerByKey('abc')

        then:
        actualShortener.destinationUrl == expectedDestinationUrl
    }


    void 'a invalid shortenerKey will find no shortener'() {

        setup:
        createShortener(shortenerKey: 'abc')

        when:
        def actualShortener = service.findActiveShortenerByKey('invalidKey')

        then:
        !actualShortener
    }

    void "an active shortener with no validUntil value set will be redirected"() {

        given:
        createShortener(validUntil: null)

        when:
        def actualShortener = service.findActiveShortenerByKey('abc')

        then:
        actualShortener
    }


    void 'a future shortener will find no shortener'() {

        setup:
        def tomorrow = new Date() + 1
        createShortener(shortenerKey: 'abc', validFrom: tomorrow)

        when:
        def actualShortener = service.findActiveShortenerByKey('abc')

        then:
        !actualShortener
    }

    void 'a past shortenerKey will find no shortener'() {

        setup:
        def yesterday = new Date() - 1
        createShortener(shortenerKey: 'abc', validUntil: yesterday)

        when:
        def actualShortener = service.findActiveShortenerByKey('abc')

        then:
        !actualShortener
    }


    def createShortener(Map params) {
        def now = new Date()
        def user = User.findByUsername("user")

        def defaultValues = [
                shortenerKey: 'abc',
                destinationUrl: 'http://example.com',
                userCreated: user,
                validFrom: now,
                validUntil: now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }
}
