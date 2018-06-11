package io.threeohone.security

import spock.lang.Specification

class UserEditCommandSpec extends Specification {

    def userEditCommand = new PasswordChangeCommand(username: 'username')

    def 'password and confirmPassword is the same'() {
        when:
        userEditCommand.password = 'password'
        userEditCommand.confirmPassword = 'password'
        userEditCommand.validate()

        then:
        !userEditCommand.hasErrors()
    }

    def 'different passwords'() {
        when:
        userEditCommand.password = 'password'
        userEditCommand.confirmPassword = 'noTheSame'
        userEditCommand.validate()

        then:
        userEditCommand.hasErrors()
        userEditCommand.errors['password'].code == 'invalid.matchingpasswords'
    }

    def 'confirmPassword can not be blank'() {
        when:
        userEditCommand.password = 'password'
        userEditCommand.confirmPassword = ''
        userEditCommand.validate()

        then:
        userEditCommand.hasErrors()
        userEditCommand.errors['confirmPassword'].code == 'blank'
    }

    def 'password can not be blank'() {
        when:
        userEditCommand.password = ''
        userEditCommand.confirmPassword = 'password'
        userEditCommand.validate()

        then:
        userEditCommand.hasErrors()
        userEditCommand.errors['password'].code == 'blank'
    }
}
