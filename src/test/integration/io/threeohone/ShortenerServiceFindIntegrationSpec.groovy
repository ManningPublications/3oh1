package io.threeohone

import io.threeohone.security.User
import spock.lang.*

class ShortenerServiceFindIntegrationSpec extends Specification {


    private service

    def setup() {
        service  = new ShortenerService()
    }

    void 'a valid key will find the correct shortener'() {

        setup:

        def expectedDestinationUrl = 'http://example.com'
        createShortener(key: 'abc', destinationUrl: expectedDestinationUrl)

        when:
        def actualShortener = this.service.findActiveShortenerByKey('abc')

        then:
        actualShortener.destinationUrl == expectedDestinationUrl
    }


    void 'a invalid key will find no shortener'() {

        setup:
        createShortener(key: 'abc')

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
        createShortener(key: 'abc', validFrom: tomorrow)

        when:
        def actualShortener = service.findActiveShortenerByKey('abc')

        then:
        !actualShortener
    }

    void 'a past key will find no shortener'() {

        setup:
        def yesterday = new Date() - 1
        createShortener(key: 'abc', validUntil: yesterday)

        when:
        def actualShortener = service.findActiveShortenerByKey('abc')

        then:
        !actualShortener
    }


    def createShortener(Map params) {
        def now = new Date()
        def user = User.findByUsername("user")

        def defaultValues = [
                key: 'abc',
                destinationUrl: 'http://example.com',
                userCreated: user,
                validFrom: now,
                validUntil: now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }
}
