package io.threeohone.security

@grails.validation.Validateable
class UserCreateCommand {
    String username
    String password
    String confirmPassword

    Role role

    static constraints = {
        password blank: false, nullable: false, password: true, validator: { password, obj ->
            def confirm = obj.confirmPassword
            confirm == password ? true : ['invalid.matchingpasswords']
        }
        confirmPassword blank: false, nullable: false, password: true
    }
}
