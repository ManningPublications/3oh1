package io.threeohone

import io.threeohone.security.Role
import io.threeohone.security.User
import io.threeohone.security.UserRole
import spock.lang.Specification

class ShortenerSearchServiceIntegrationSpec extends Specification {


    def 'find shortener by destinationUrl with max 5 and offset 5'() {

        given:
        def validity = Shortener.Validity.ACTIVE

        when:
        def results = service.search('test', validity, createDefaultParams(max: 5, offset: 5))

        then:
        results.size() == 5
        results*.destinationUrl*.contains('test5').count(true) == 1
        results*.destinationUrl*.contains('test6').count(true) == 1
        results*.destinationUrl*.contains('test7').count(true) == 1
        results*.destinationUrl*.contains('test8').count(true) == 1
        results*.destinationUrl*.contains('test9').count(true) == 1
    }

    def 'find shortener by destinationUrl with max 5'() {

        given:
        def query = 'test'

        when:
        def results = service.search(query, Shortener.Validity.ACTIVE, createDefaultParams(max: 5))

        then:
        results.size() == 5
        !results*.destinationUrl*.contains('test').contains(false)
    }

    def 'find shortener by destinationUrl and username'() {

        given:
        createShortener(destinationUrl: 'http://www.testUrl.com', userCreated: createTestUser('username'))

        when:
        def results = service.search('testUrl username', Shortener.Validity.ACTIVE, createDefaultParams())

        then:
        results.size() == 1

        and:
        results[0].destinationUrl.contains('testUrl.com')
        results[0].userCreated.username.contains('username')
    }

    def 'one search query hat leads to no results - no results are shown even if the other queries would show results'() {

        given:
        createShortener(destinationUrl: 'http://www.testUrl.com', userCreated: createTestUser('username'))
        def query = 'testUrl username someThingNotBeFound'

        when:
        def results = service.search(query, Shortener.Validity.ACTIVE, createDefaultParams())

        then:
        results.isEmpty()
    }

    def 'find shortener by destinationUrl'() {

        given:
        createShortener(destinationUrl: 'http://www.myDestinationUrl.com')

        when:
        def results = service.search('myDestinationUrl', Shortener.Validity.ACTIVE, createDefaultParams())

        then:
        results.size() == 1
        results.first().destinationUrl.contains('myDestinationUrl')
    }


    def 'a shortener can be found by the username as the search query'() {

        setup: 'create new shortener for tom'
        def tom = createTestUser('tom')
        createShortener(destinationUrl: 'http://www.tom.com', userCreated: tom)

        when:
        def results = service.search('tom', Shortener.Validity.ACTIVE, createDefaultParams())

        then:
        results.size() == 1
        results.first().destinationUrl == 'http://www.tom.com'
        results.first().userCreated == tom
    }

    def 'searchByUser does an additional search on the specified user'() {

        given: 'there is a google shortener for tom'
        def tom = createTestUser('tom')
        createShortener(key: 'first', destinationUrl: 'http://www.google.com', userCreated: tom)

        and: 'there is a google shortener for nick'
        createShortener(destinationUrl: 'http://www.google.com', userCreated: createTestUser('nick'))

        when: 'search for google is executed'
        def results = service.searchByUser('google', Shortener.Validity.ACTIVE, tom, createDefaultParams())

        then: 'all results for tom are displayed'
        results.size() == 1

        and: 'the first shortener has the correct user'
        results.first().destinationUrl == 'http://www.google.com'
        results.first().userCreated == tom

    }


    def 'find a future shortener'() {

        given: 'create new shortener for tom'
        def tom = createTestUser('tom')
        createFutureShortener(destinationUrl: 'http://www.tom.com', userCreated: tom)

        when:
        def results = service.search('tom', Shortener.Validity.FUTURE, createDefaultParams())

        then:
        results.size() == 1
        results.first().destinationUrl == 'http://www.tom.com'
        results.first().userCreated == tom
    }


    def "when no validity is given all shorteners are found"() {

        given:
        def now = new Date()
        createShortener(key: 'expired', validFrom: now - 2, validUntil: now + 1)
        createShortener(key: 'active', validFrom: now - 1, validUntil: now + 1)
        createShortener(key: 'future', validFrom: now + 1, validUntil: now + 2)

        when: "i search for example.com without validity"
        def results = service.search('example.com', null, createDefaultParams())

        then: "there are three results"
        results.size() == 3
    }


    def createShortener(Map params) {
        def now = new Date()
        def user = User.findByUsername('user')

        def defaultValues = [
                key           : 'abc',
                destinationUrl: 'http://example.com',
                userCreated   : user,
                validFrom     : now,
                validUntil    : now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }

    def createFutureShortener(Map params) {
        def now = new Date()
        params.validFrom = now + 1
        params.validUntil = now + 2
        createShortener(params)
    }

    User createTestUser(username) {
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def user = User.findByUsername(username) ?: new User(username: username, password: 'user', enabled: true).save(failOnError: true, flush: true)
        if (!user.authorities.contains(userRole)) {
            UserRole.create user, userRole
        }
        user
    }


    Map createDefaultParams(Map params = [:]) {
        def defaults = [
                max   : 10,
                offset: 0,
                sort  : 'id',
                order : 'asc'
        ]

        return defaults + params

    }

}
