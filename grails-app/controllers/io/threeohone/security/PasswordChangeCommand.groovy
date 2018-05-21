package io.threeohone.security

import grails.validation.Validateable

class PasswordChangeCommand implements Validateable {
    String username
    String password
    String confirmPassword

    static constraints = {
        username editable: false

        password blank: false, nullable: false, password: true, validator: { password, obj ->
            def confirm = obj.confirmPassword
            confirm == password ? true : ['invalid.matchingpasswords']
        }
        confirmPassword blank: false, nullable: false, password: true
    }
}
