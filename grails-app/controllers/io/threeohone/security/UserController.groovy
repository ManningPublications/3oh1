package io.threeohone.security

import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.web.servlet.HttpHeaders

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
class UserController {


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

        def passwordChangeCommand = new PasswordChangeCommand(username: userInstance.username)
        respond passwordChangeCommand
    }

    @Transactional
    def update(PasswordChangeCommand passwordChangeCommand) {
        if (passwordChangeCommand == null) {
            notFound()
            return
        }

        if (passwordChangeCommand.hasErrors()) {
            respond passwordChangeCommand.errors, view: 'edit'
            return
        }

        def userInstance = User.findByUsername(passwordChangeCommand.username)

        if (userInstance == null) {
            notFound()
            return
        }

        userInstance.password = passwordChangeCommand.password
        userInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'password.updated.message', args: [message(code: 'user.label'), passwordChangeCommand.username])
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

