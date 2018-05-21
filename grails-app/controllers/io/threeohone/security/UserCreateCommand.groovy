package io.threeohone.security

import grails.validation.Validateable

class UserCreateCommand implements Validateable {
    String username
    String password
    String confirmPassword

    Role role

    static constraints = {
        role nullable: true
        password blank: false, nullable: false, password: true, validator: { password, obj ->
            def confirm = obj.confirmPassword
            confirm == password ? true : ['invalid.matchingpasswords']
        }
        confirmPassword blank: false, nullable: false, password: true
    }
}
