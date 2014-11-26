package io.threeohone.security

@grails.validation.Validateable
class UserChangeCommand {
    String username
    Role role

    static constraints = {
        username editable: false
    }
}
