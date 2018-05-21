package io.threeohone.security

import grails.validation.Validateable

class UserChangeCommand implements Validateable {
    String username
    Role role

    static constraints = {
        username editable: false
    }
}
