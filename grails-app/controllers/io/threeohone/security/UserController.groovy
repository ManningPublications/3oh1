package io.threeohone.security

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['isAuthenticated()'])
class UserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model: [userInstanceCount: User.count()]
    }

    def show(User userInstance) {
        respond userInstance
    }

    def create() {
        def role = Role.findByAuthority('ROLE_USER')
        def userCreateCommand = new UserCreateCommand(roleId: role.id)

        respond userCreateCommand
    }

    @Transactional
    def save(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        if (userInstance.hasErrors()) {
            respond userInstance.errors, view: 'create'
            return
        }

        userInstance.save flush: true

        def role = Role.findById(params.roleId)
        UserRole.create(userInstance, role).save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])
                redirect userInstance
            }
            '*' { respond userInstance, [status: CREATED] }
        }
    }

    def edit(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }

        def userEditCommand = new UserEditCommand(
                username: userInstance.username,
                version: userInstance.version)
        respond userEditCommand
    }

    @Transactional
    def update(UserEditCommand userEditCommand) {
        if (userEditCommand == null) {
            notFound()
            return
        }

        if (userEditCommand.hasErrors()) {
            respond userEditCommand.errors, view: 'edit'
            return
        }

        def userInstance = User.findByUsername(userEditCommand.username)
        userInstance.password = userEditCommand.password
        userInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'password.updated.message', args: [message(code: 'user.label', default: 'User'), userEditCommand.username])
                redirect userInstance
            }
            '*' { respond userInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(User userInstance) {

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
