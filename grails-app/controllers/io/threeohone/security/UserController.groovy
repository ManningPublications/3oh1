package io.threeohone.security

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class UserController {

    def springSecurityService

    static responseFormats = ['html', 'json']

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        List<User>  users = User.withTransaction{ User.list(params) }
        respond users, model: [userInstanceCount: User.count()]
    }

    def show() {
        respond User.withTransaction{ User.findByUsername(params.id) }
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

