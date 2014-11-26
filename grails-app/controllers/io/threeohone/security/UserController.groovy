package io.threeohone.security

import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.web.servlet.HttpHeaders

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class UserController {

    def springSecurityService

    static responseFormats = ['html', 'json']

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model: [userInstanceCount: User.count()]
    }

    def show() {
        respond User.findByUsername(params.id)
    }

    def create() {
        def userCreateCommand = new UserCreateCommand()

        respond userCreateCommand
    }

    @Transactional
    def save(UserCreateCommand userCreateCommand) {

        if (userCreateCommand.hasErrors()) {
            respond userCreateCommand.errors, view: 'create'
            return
        }

        User userInstance = new User()
        bindData(userInstance, userCreateCommand)

        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.save flush: true

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view: 'create'
            return
        }

        def role = userCreateCommand.role ?: Role.findByAuthority('ROLE_USER')
        UserRole.create(userInstance, role).save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label'), userInstance.username])
                redirect url: [resource: "user", action: "show", id: userInstance.username]
            }
            '*' {

                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: "user", action: 'show', id: userInstance.username))
                respond userInstance, [status: CREATED]
            }
        }
    }

    def edit() {
        User userInstance = User.findByUsername(params.id)

        if (userInstance == null) {
            notFound()
            return
        }

        def roleChangeCommand = new UserChangeCommand(username: userInstance.username, role: UserRole.findByUser(userInstance).role)
        respond roleChangeCommand
    }

    @Transactional
    def update(UserChangeCommand userChangeCommand) {

        if (userChangeCommand == null) {
            notFound()
            return
        }

        if (userChangeCommand.hasErrors()) {
            respond userChangeCommand.errors, view: 'edit'
            return
        }

        def userInstance = User.findByUsername(userChangeCommand.username)

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
            if the user-role is the same the user-role had to be removed before a new will be created
            that will only work in a new transaction
        */
        UserRole.withNewTransaction {
            UserRole.remove(userInstance, userRole.role)
        }

        UserRole.withNewTransaction {
            UserRole.create(userInstance, userChangeCommand.role)
        }

        if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
            springSecurityService.reauthenticate springSecurityService.currentUser.username

        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label'), userChangeCommand.username])
                redirect url: [resource: "user", action: "show", id: userInstance.username]
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    @Transactional
    def delete() {

        User userInstance = User.findByUsername(params.id)

        if (userInstance == null) {
            notFound()
            return
        }

        UserRole.findByUser(userInstance)?.delete flush: true
        userInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
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

