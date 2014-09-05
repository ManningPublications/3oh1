package com.manning

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

class ShortenerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    ShortenerService shortenerService

    def index(Integer max) {
        params.validity = params.validity ?: 'active'
        params.max = Math.min(max ?: 10, 100)

        def shortenerList = Shortener.withValidityState(params.validity, params)

        respond shortenerList.list(params), model: [shortenerInstanceCount: shortenerList.count()]
    }

    def show(Shortener shortenerInstance) {
        respond shortenerInstance
    }

    def create() {
        respond new Shortener(params)
    }

    def save() {


        def shortenerInstance = shortenerService.createShortener(params)

        if (shortenerInstance.hasErrors()) {
            respond shortenerInstance.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'shortener.label'), shortenerInstance.shortenerKey])
                redirect shortenerInstance
            }
            '*' { respond shortenerInstance, [status: CREATED] }
        }
    }

    def edit(Shortener shortenerInstance) {
        respond shortenerInstance
    }

    @Transactional
    def update(Shortener shortenerInstance) {
        if (shortenerInstance == null) {
            notFound()
            return
        }

        if (shortenerInstance.hasErrors()) {
            respond shortenerInstance.errors, view: 'edit'
            return
        }

        shortenerInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'shortener.label'), shortenerInstance.shortenerKey])
                redirect shortenerInstance
            }
            '*' { respond shortenerInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Shortener shortenerInstance) {

        if (shortenerInstance == null) {
            notFound()
            return
        }

        shortenerInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'shortener.label'), shortenerInstance.shortenerKey])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'shortener.label'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
