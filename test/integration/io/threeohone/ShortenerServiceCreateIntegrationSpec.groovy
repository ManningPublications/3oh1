package io.threeohone

import fm.jiecao.lib.Hashids
import io.threeohone.security.User
import grails.plugin.springsecurity.SpringSecurityUtils
import spock.lang.Specification

class ShortenerServiceCreateIntegrationSpec extends Specification {

    ShortenerService service
    def springSecurityService


    def setup() {
        service = new ShortenerService()
        service.springSecurityService = springSecurityService

    }

    void "createShortener creates a shortener with a persisted shortenerKey"() {

        given:
        def params = [
                destinationUrl: "http://www.example.com",
                validFrom: new Date(),
                validUntil: new Date() + 1,
        ]

        when:
        def persistedShortener
        SpringSecurityUtils.doWithAuth("user") {
            persistedShortener = service.createShortener(params)
        }

        then:
        !persistedShortener.hasErrors()

        when:
        def expectedShortenerKey = new Hashids(ShortenerService.DEFAULT_SALT).encode(persistedShortener.id)

        then:
        persistedShortener.shortenerKey == expectedShortenerKey

    }



    void "createShortener returns the unsaved shortener if it has invalid properties"() {

        expect:
        service.createShortener(destinationUrl: "notAValidDomain").hasErrors()
    }





    void "importExistingShortener can create a shortener with a given shortenerKey"() {

        given:
        def params = [
                destinationUrl: "http://www.example.com",
                shortenerKey: "myTestShortenerKey",
                validFrom: new Date(),
                validUntil: new Date() + 1,
        ]

        when:
        def persistedShortener
        SpringSecurityUtils.doWithAuth("user") {
            persistedShortener = service.importExistingShortener(params)
        }

        then:
        !persistedShortener.hasErrors()

        and:
        persistedShortener.shortenerKey == params.shortenerKey
    }



    void "importExistingShortener returns the unsaved shortener if it has invalid properties"() {

        expect:
        service.createShortener(destinationUrl: "notAValidDomain", shortenerKey: new Date()).hasErrors()
    }

    void "importExistingShortener does not save the shortener if there is already a shorener with this shortenerKey"() {

        given:
        def params = [
                destinationUrl: "http://www.example.com",
                shortenerKey: "myTestShortenerKey",
                validFrom: new Date(),
        ]
        def persistedShortener
        SpringSecurityUtils.doWithAuth("user") {
            persistedShortener = service.importExistingShortener(params)
        }


        when:
        params = [
                destinationUrl: "http://www.example.com",
                shortenerKey: "myTestShortenerKey",
                validFrom: new Date(),
        ]
        Shortener notUniqueShortener
        SpringSecurityUtils.doWithAuth("user") {
            notUniqueShortener = service.importExistingShortener(params)
        }
        then:
        notUniqueShortener.hasErrors()

        and:
        notUniqueShortener.errors.getFieldError("shortenerKey").codes.contains("unique")
    }
}
