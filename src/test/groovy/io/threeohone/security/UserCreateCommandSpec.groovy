package io.threeohone.security

import spock.lang.Specification

class UserCreateCommandSpec extends Specification {
    def userCreateCommand = new UserCreateCommand(username: 'username', role: new Role(authority: "admin"))

    def 'when password and confirmPassword match, there are no validation errors'() {
        when:
        userCreateCommand.password = 'password'
        userCreateCommand.confirmPassword = 'password'
        userCreateCommand.validate()

        then:
        !userCreateCommand.hasErrors()
    }

    def 'when password and confirmPassword dont match, there is a validation error'() {
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
