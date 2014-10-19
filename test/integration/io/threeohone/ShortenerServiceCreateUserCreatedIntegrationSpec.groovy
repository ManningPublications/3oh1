package io.threeohone

import fm.jiecao.lib.Hashids
import grails.plugin.springsecurity.SpringSecurityUtils
import io.threeohone.security.User
import spock.lang.Specification

class ShortenerServiceCreateUserCreatedIntegrationSpec extends Specification {

    ShortenerService service
    def springSecurityService
    User basicAuthUser
    ShortenerCreateCommand createCommand
    User ownerOfTheShortener


    def setup() {
        service = new ShortenerService()
        service.springSecurityService = springSecurityService

        basicAuthUser = new User(username: "basicAuthUser", password: "basicAuthUser", enabled: true).save(failOnError: true)
        ownerOfTheShortener = new User(username: "ownerOfTheShortener", password: "ownerOfTheShortener", enabled: true).save(failOnError: true)

        createCommand  = new ShortenerCreateCommand(
                destinationUrl: "http://www.example.com",
                validFrom: new Date(),
                validUntil: new Date() + 1
        )

    }


    void "createShortener assigns the current user to shortener instance if no username is sent"() {

        given: "the sent user is null"
        createCommand.userCreated = null


        when: "the shortener is created with the basiAuthUser as login"
        def persistedShortener
        SpringSecurityUtils.doWithAuth("basicAuthUser") {
            persistedShortener = service.createShortener(createCommand)
        }

        then: "the basicAuthUser is the owner of the shortener"
        persistedShortener.userCreated == basicAuthUser

    }


    void "createShortener assigns the sent user to shortener instance if it exists"() {

        given:  "ownerOfTheShortener is sent as the userCreated"
        createCommand.userCreated = ownerOfTheShortener.username

        when: "the shortener is created with the basiAuthUser as login"
        def persistedShortener
        SpringSecurityUtils.doWithAuth("basicAuthUser") {
            persistedShortener = service.createShortener(createCommand)
        }

        then: "the ownerOfTheShortener is the owner of the shortener"
        persistedShortener.userCreated == ownerOfTheShortener

    }


    void "createShortener assigns the current user if the sent user is not found"() {

        given:  "ownerOfTheShortener is sent as the userCreated"
        createCommand.userCreated = "aWrongUsername"

        when: "the shortener is created with the basiAuthUser as login"
        def persistedShortener
        SpringSecurityUtils.doWithAuth("basicAuthUser") {
            persistedShortener = service.createShortener(createCommand)
        }

        then: "the ownerOfTheShortener is the owner of the shortener"
        persistedShortener.userCreated == basicAuthUser

    }


}
