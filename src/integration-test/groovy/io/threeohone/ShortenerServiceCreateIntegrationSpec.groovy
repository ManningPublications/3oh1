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

    void "createShortener creates a shortener with a persisted key"() {

        given:
        def createCommand = new ShortenerCommand(
                destinationUrl: "http://www.example.com",
                validFrom: new Date(),
                validUntil: new Date() + 1,
        )

        when:
        def persistedShortener
        SpringSecurityUtils.doWithAuth("user") {
            persistedShortener = service.createShortener(createCommand)
        }

        then:
        !persistedShortener.hasErrors()

        when:
        def expectedKey = new Hashids(ShortenerService.DEFAULT_SALT).encode(persistedShortener.id)

        then:
        persistedShortener.key == expectedKey

    }



    void "createShortener returns the unsaved shortener if it has invalid properties"() {

        given:
        def createCommand = new ShortenerCommand(destinationUrl: "notAValidDomain")

        expect:
        service.createShortener(createCommand).hasErrors()
    }



    void "importExistingShortener returns the unsaved shortener if it has invalid properties"() {

        given:
        def createCommand = new ShortenerCommand(destinationUrl: "notAValidDomain", key: new Date())

        expect:
        service.importExistingShortener(createCommand).hasErrors()
    }




    void "importExistingShortener can create a shortener with a given key"() {

        given:
        def createCommand = new ShortenerCommand(
                destinationUrl: "http://www.example.com",
                key: "myTestKey",
                validFrom: new Date(),
                validUntil: new Date() + 1
        )

        when:
        def persistedShortener
        SpringSecurityUtils.doWithAuth("user") {
            persistedShortener = service.importExistingShortener(createCommand)
        }

        then:
        !persistedShortener.hasErrors()

        and:
        persistedShortener.key == createCommand.key
    }

    void "importExistingShortener does not save the shortener if there is already a shorener with this key"() {

        given: "a shortener with key myTestKey is imported"
        def createCommand = new ShortenerCommand(
                destinationUrl: "http://www.example.com",
                key: "myTestKey",
                validFrom: new Date()
        )

        def persistedShortener
        SpringSecurityUtils.doWithAuth("user") {
            persistedShortener = service.importExistingShortener(createCommand)
        }


        when: "another shortener should be created with the same key"
        createCommand = new ShortenerCommand(
                destinationUrl: "http://www.example.com",
                key: "myTestKey",
                validFrom: new Date()
        )

        Shortener notUniqueShortener
        SpringSecurityUtils.doWithAuth("user") {
            notUniqueShortener = service.importExistingShortener(createCommand)
        }

        then: "the second shortener could not be saved"
        notUniqueShortener.hasErrors()

        and: "there is a unique error message"
        notUniqueShortener.errors.getFieldError("key").codes.contains("unique")
    }
}
