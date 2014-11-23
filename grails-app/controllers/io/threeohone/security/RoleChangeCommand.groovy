package io.threeohone.security

@grails.validation.Validateable
class RoleChangeCommand {
    String username
    Role role

    static constraints = {
        username editable: false
    }
}
