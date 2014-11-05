package io.threeohone.security

@grails.validation.Validateable
class PasswordChangeCommand {
    String username
    String password
    String confirmPassword
    Long version

    static constraints = {
        username editable: false

        password blank: false, nullable: false, password: true, validator: { password, obj ->
            def confirm = obj.confirmPassword
            confirm == password ? true : ['invalid.matchingpasswords']
        }
        confirmPassword blank: false, nullable: false, password: true
    }
}
