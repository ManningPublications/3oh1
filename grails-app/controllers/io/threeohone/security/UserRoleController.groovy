package io.threeohone.security

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.servlet.HttpHeaders

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_ADMIN'])
class UserRoleController {
    def springSecurityService

    static responseFormats = ['html', 'json']

    static allowedMethods = [update: "PUT"]

    def edit() {
        User userInstance = User.findByUsername(params.id)

        if (userInstance == null) {
            notFound()
            return
        }

        def roleChangeCommand = new RoleChangeCommand(username: userInstance.username, role: UserRole.findByUser(userInstance).role)
        respond roleChangeCommand
    }

    @Transactional
    def update(RoleChangeCommand roleChangeCommand) {

        if (roleChangeCommand == null) {
            notFound()
            return
        }

        if (roleChangeCommand.hasErrors()) {
            respond roleChangeCommand.errors, view: 'edit'
            return
        }

        def userInstance = User.findByUsername(roleChangeCommand.username)

        if (userInstance == null) {
            notFound()
            return
        }

        def userRole = UserRole.findByUser(userInstance)

        if (userRole == null) {
            notFound()
            return
        }

        /*
        if the user-role is the same
        the user-role had to be removed before a new will be created
        that will only work in a new transaction
        */
        UserRole.withNewTransaction {
            UserRole.remove(userInstance, userRole.role)
        }

        UserRole.withNewTransaction {
            UserRole.create(userInstance, roleChangeCommand.role)
        }

        if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
            springSecurityService.reauthenticate springSecurityService.currentUser.username
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label'), roleChangeCommand.username])
                redirect url: [resource: "user", action: "show", id: userInstance.username]
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}

