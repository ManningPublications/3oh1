package com.manning

import hashids.HashidsService
import spock.lang.Specification

class ShortenerServiceIntegrationSpec extends Specification {

    def grailsApplication
    ShortenerService service

    def setup() {
        service = new ShortenerService()
        service.hashidsService = createHashIdsService()

    }

    void "createShortener creates a shortener with a persisted shortenerKey"() {

        given:
        def params = [
                destinationUrl: "http://www.example.com",
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: "Dummy User"
        ]

        when:
        def persistedShortener = service.createShortener(params)
        def expectedShortenerKey = createHashIdsService().encrypt(persistedShortener.id)

        then:
        persistedShortener.shortenerKey == expectedShortenerKey

    }

    void "createShortener returns the unsaved shortener if it has invalid properties"() {

        expect:
        service.createShortener(destinationUrl: "notAValidDomain").hasErrors()
    }

    protected HashidsService createHashIdsService() {

        def config = grailsApplication.mergedConfig
        config.grails.plugin.hashids.alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        config.grails.plugin.hashids.salt = "manning"
        config.grails.plugin.hashids.anemic_domain = false
        config.grails.plugin.hashids.min_hash_length = 3
        config.grails.plugin.hashids.id_field = 'id'
        def hashidsService = new HashidsService(grailsApplication:grailsApplication)
        hashidsService.init()

        return hashidsService
    }
}