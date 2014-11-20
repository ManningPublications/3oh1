package io.threeohone.security

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.transaction.annotation.Transactional
import static org.springframework.http.HttpStatus.*


@Secured(['isFullyAuthenticated()'])
class PasswordController {


    def springSecurityService

    static responseFormats = ['html']

    static allowedMethods = [update: "PUT"]

    def index() {

        def userInstance = User.findByUsername(params.id)

        if (!userInstance) {
            notFound()
            return
        }

        def passwordChangeCommand = new PasswordChangeCommand(username: userInstance.username)

        respond passwordChangeCommand
    }


    @Transactional
    def update(PasswordChangeCommand passwordChangeCommand) {
        if (passwordChangeCommand == null) {
            println "passwordChangeCommand == null"
            notFound()
            return
        }

        if (passwordChangeCommand.hasErrors()) {

            println "passwordChangeCommand.hasErrors()"
            respond passwordChangeCommand.errors, view: 'edit'
            return
        }

        def userInstance = User.findByUsername(passwordChangeCommand.username)


        if (!hasCurrentUserAdminRole()) {
            println "!hasCurrentUserAdminRole"
            def currentUserUsername = springSecurityService.getCurrentUser().username

            if (currentUserUsername != userInstance.username) {

                println "accessDenied"
                accessDenied()
                return
            }
        }

        if (userInstance == null) {

            println "userInstance == null"
            notFound()
            return
        }

        userInstance.password = passwordChangeCommand.password
        userInstance.save flush: true

        println userInstance

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'password.updated.message', args: [message(code: 'user.label'), passwordChangeCommand.username])
                redirect controller: "shortener", action: "index", method: "GET"
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    private boolean hasCurrentUserAdminRole() {
        def adminRoleId = Role.findByAuthority("ROLE_ADMIN")?.id
        def userId = springSecurityService.loadCurrentUser()?.id

        if (userId && adminRoleId) {
            return UserRole.exists(userId, adminRoleId)
        }

        return false

    }

    protected void accessDenied() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'springSecurity.denied.message')
                redirect controller: "shortener", action: "index", method: "GET"
            }
            '*' { render status: FORBIDDEN }
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
