package io.threeohone.security

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.transaction.annotation.Transactional
import static org.springframework.http.HttpStatus.*


@Secured(['isFullyAuthenticated()'])
class PasswordController {


    def springSecurityService

    static responseFormats = ['html']

    static allowedMethods = [edit: "GET", update: "PUT"]

    def edit() {

        def userInstance = User.findByUsername(params.userId)

        if (!userInstance) {
            notFound()
            return
        }

        if (!isCurrentUserAllowedToChangeTheUser(userInstance)) {
            accessDenied()
            return
        }


        def passwordChangeCommand = new PasswordChangeCommand(username: userInstance.username)

        respond passwordChangeCommand
    }


    @Transactional
    def update(PasswordChangeCommand passwordChangeCommand) {

        def userInstance = User.findByUsername(passwordChangeCommand?.username)

        if (!passwordChangeCommand || !userInstance) {
            notFound()
            return
        }

        if (!isCurrentUserAllowedToChangeTheUser(userInstance)) {
            accessDenied()
            return
        }


        if (passwordChangeCommand.hasErrors()) {
            respond passwordChangeCommand.errors, view: 'edit'
            return
        }


        userInstance.password = passwordChangeCommand.password
        userInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'password.updated.message', args: [message(code: 'user.label'), passwordChangeCommand.username])
                redirect controller: "shortener", action: "index", method: "GET"
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    private boolean isCurrentUserAllowedToChangeTheUser(User userInstance) {
        def currentUserUsername = springSecurityService.getCurrentUser().username

        if (!hasCurrentUserAdminRole() && currentUserUsername != userInstance.username) {
            return false
        }

        return true
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
        flash.error = message(code: 'springSecurity.denied.message')
        redirect controller: "shortener", action: "index", method: "GET"
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
