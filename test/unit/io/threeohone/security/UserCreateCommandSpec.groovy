package io.threeohone.security

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class UserCreateCommandSpec extends Specification {
    def userCreateCommand = new UserCreateCommand(version: 0, username: 'username', roleId: 1)

    def 'password and confirmPassword is the same'() {
        when:
        userCreateCommand.password = 'password'
        userCreateCommand.confirmPassword = 'password'
        userCreateCommand.validate()

        then:
        !userCreateCommand.hasErrors()
    }

    def 'different passwords'() {
        when:
        userCreateCommand.password = 'password'
        userCreateCommand.confirmPassword = 'noTheSame'
        userCreateCommand.validate()

        then:
        userCreateCommand.hasErrors()
        userCreateCommand.errors['password'].code == 'invalid.matchingpasswords'
    }

    def 'confirmPassword can not be blank'() {
        when:
        userCreateCommand.password = 'password'
        userCreateCommand.confirmPassword = ''
        userCreateCommand.validate()

        then:
        userCreateCommand.hasErrors()
        userCreateCommand.errors['confirmPassword'].code == 'blank'
    }

    def 'password can not be blank'() {
        when:
        userCreateCommand.password = ''
        userCreateCommand.confirmPassword = 'password'
        userCreateCommand.validate()

        then:
        userCreateCommand.hasErrors()
        userCreateCommand.errors['password'].code == 'blank'
    }

}