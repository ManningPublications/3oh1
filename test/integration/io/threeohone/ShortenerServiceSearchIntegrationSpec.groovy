package io.threeohone

import grails.test.mixin.TestFor
import io.threeohone.security.Role
import io.threeohone.security.User
import io.threeohone.security.UserRole
import spock.lang.Specification

@TestFor(ShortenerSearchService)
class ShortenerServiceSearchIntegrationSpec extends Specification {


    def 'find shortener by destinationUrl with max= 5 and offset 5'() {
        setup:
        def max = 5
        def offset = 5
        def query = 'test'
        def validity = Shortener.Validity.ACTIVE
        def sort = 'id'
        def order = 'asc'

        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 5
        results*.destinationUrl*.contains('test5').count(true) == 1
        results*.destinationUrl*.contains('test6').count(true) == 1
        results*.destinationUrl*.contains('test7').count(true) == 1
        results*.destinationUrl*.contains('test8').count(true) == 1
        results*.destinationUrl*.contains('test9').count(true) == 1
    }

    def 'find shortener by destinationUrl with max= 5'() {
        setup:
        def max = 5
        def offset = 0
        def query = 'test'
        def validity = Shortener.Validity.ACTIVE
        def sort = 'id'
        def order = 'asc'

        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 5
        !results*.destinationUrl*.contains('test').contains(false)
    }

    def 'find shortener by destinationUrl and username'() {
        setup:
        def max = 10
        def offset = 0
        def query = 'twitter user'
        def validity = Shortener.Validity.ACTIVE
        def sort = 'id'
        def order = 'asc'

        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 1

        and:
        results[0].destinationUrl.contains('twitter.com')
        results[0].userCreated.username.contains('user')
    }

    def 'find nothing'() {
        setup:
        def max = 10
        def offset = 0
        def query = 'twitter user someThingNotBeFound'
        def validity = Shortener.Validity.ACTIVE
        def sort = 'id'
        def order = 'asc'

        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.isEmpty()
    }

    def 'find shortener by destinationUrl'() {
        setup:
        def max = 10
        def offset = 0
        def query = 'goo'
        def validity = Shortener.Validity.ACTIVE
        def sort = 'id'
        def order = 'asc'


        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 1
        results.first().destinationUrl.contains('google')
    }

    def 'find shortener by userName'() {
        setup: 'create new shortener for tom'
        def now = new Date()
        def expectedUserName = 'tom'
        def tom = createTestUser(expectedUserName)
        def expectedShortener = new Shortener(shortenerKey: 'abc',
                destinationUrl: 'http://www.tom.com',
                userCreated: tom,
                validFrom: now,
                validUntil: now + 1).save(failOnError: true, flush: true)

        and: 'set params and query'
        def max = 10
        def offset = 0
        def query = 'tom'
        def validity = Shortener.Validity.ACTIVE
        def sort = 'id'
        def order = 'asc'


        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 1
        results.first().destinationUrl == 'http://www.tom.com'
        results.first().userCreated.username == 'tom'
    }

    def 'find a shortener which is active in future'() {
        setup: 'create new shortener for tom'
        def now = new Date()
        def expectedUserName = 'tom'
        def tom = createTestUser(expectedUserName)
        def expectedShortener = new Shortener(shortenerKey: 'abc',
                destinationUrl: 'http://www.tom.com',
                userCreated: tom,
                validFrom: now + 1,
                validUntil: now + 2).save(failOnError: true, flush: true)

        and: 'set params and query'
        def max = 10
        def offset = 0
        def query = 'tom'
        def validity = Shortener.Validity.FUTURE
        def sort = 'id'
        def order = 'asc'


        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 1
        results.first().destinationUrl == 'http://www.tom.com'
        results.first().userCreated.username == 'tom'
    }

    def 'find a shortener which is active in future by selecting all'() {
        setup: 'create new shortener'
        def now = new Date()
        def expectedUserName = 'tom'
        def tom = createTestUser(expectedUserName)
        def expectedShortener = new Shortener(shortenerKey: 'abc',
                destinationUrl: 'http://www.tom.com',
                userCreated: tom,
                validFrom: now + 1,
                validUntil: now + 2).save(failOnError: true, flush: true)

        and: 'set params and query'
        def max = 10
        def offset = 0
        def query = 'tom'
        def validity = null
        def sort = 'id'
        def order = 'asc'


        when:
        def results = service.search(query, validity, max, offset, sort, order)

        then:
        results.size() == 1
        results.first().destinationUrl == 'http://www.tom.com'
        results.first().userCreated.username == 'tom'
    }

    def createShortener(Map params) {
        def now = new Date()
        def user = User.findByUsername('user')

        def defaultValues = [
                shortenerKey  : 'abc',
                destinationUrl: 'http://example.com',
                userCreated   : user,
                validFrom     : now,
                validUntil    : now + 1
        ]

        new Shortener(defaultValues + params).save(failOnError: true)
    }

    User createTestUser(username) {
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def user = User.findByUsername(username) ?: new User(username: username, password: 'user', enabled: true).save(failOnError: true, flush: true)
        if (!user.authorities.contains(userRole)) {
            UserRole.create user, userRole
        }
        user
    }
}
