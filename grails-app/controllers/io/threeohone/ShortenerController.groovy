package io.threeohone

import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import org.grails.plugins.quickSearch.QuickSearchService

import static org.springframework.http.HttpStatus.*

@Secured(['isAuthenticated()'])
class ShortenerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    ShortenerService shortenerService
    QuickSearchService quickSearchService

    def index(Integer max) {
        params.validity = params.validity ?: 'active'
        params.max = Math.min(max ?: 10, 100)
        params.offset = 0


        def query = params.search ?: ''

        def searchParams = [/*sort: 'destinationUrl', order: 'asc',*/ max: params.max, offset: params.offset]
        def searchProperties = [destinationUrl: 'destinationUrl',
                                shortenerKey  : 'shortenerKey',
                                validUntil    : 'validUntil',
                                validFrom     : 'validFrom']


        def customClosure = Shortener.getCustomClosureByValidity(params.validity)


        def shortenerList = quickSearchService.search(domainClass: Shortener, searchParams: searchParams,
                searchProperties: searchProperties, customClosure: customClosure, query: query)

        respond shortenerList, model: [shortenerInstanceCount: shortenerList.size()]
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

                flash.message = message(code: 'default.created.message', args: [shortener.shortUrl(shortener: shortenerInstance)])
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
                flash.message = message(code: 'default.updated.message', args: [shortener.shortUrl(shortener: shortenerInstance)])
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
                flash.message = message(code: 'default.deleted.message', args: [shortener.shortUrl(shortener: shortenerInstance)])
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
