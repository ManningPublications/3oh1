package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.servlet.HttpHeaders

import static org.springframework.http.HttpStatus.*

@Secured(['isAuthenticated()'])
class ShortenerController {

    static responseFormats = ['html', 'json']

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]


    StatisticsService statisticsService

    ShortenerService shortenerService

    def index(Integer max) {
        params.validity = params.validity ?: 'active'
        params.max = Math.min(max ?: 10, 100)
        params.offset = params.offset ?: 0
        params.search = params.search ?: ''
        params.sort = params.sort ?: 'destinationUrl'
        params.order = params.order ?: 'asc'

        def validity = Shortener.getValidityByString(params.validity)

        def shortenerList = shortenerService.search(params.search, validity, params.max, params.offset, params.sort, params.order)

        respond shortenerList, model: [shortenerInstanceCount: shortenerList.getTotalCount()]
    }


    def show(Shortener shortenerInstance) {


        def result = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)

        def totalNumberOfRedirectsPerMonth = [monthNames: [], redirectCounters: []]
        totalNumberOfRedirectsPerMonth.monthNames = result.collect { "${it.month} / ${it.year}" }
        totalNumberOfRedirectsPerMonth.redirectCounters = result.collect { it.redirectCounter }

        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()


        respond shortenerInstance, model: [
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                redirectCounter               : redirectCounter
        ]
    }

    def create() {
        respond new Shortener(params)
    }

    def save(Shortener shortenerInstance) {
        def shortenerParams = [
                shortenerKey: shortenerInstance.shortenerKey,
                destinationUrl: shortenerInstance.destinationUrl,
                validFrom: shortenerInstance.validFrom ?: new Date(),
                validUntil: shortenerInstance.validUntil
        ]

        if (shortenerInstance.shortenerKey) {
            shortenerInstance = shortenerService.importExistingShortener(shortenerParams)
        } else {
            shortenerInstance = shortenerService.createShortener(shortenerParams)
        }

        if (shortenerInstance.hasErrors()) {
            respond shortenerInstance.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {

                flash.message = message(code: 'default.created.message', args: [shortener.shortUrl(shortener: shortenerInstance)])
                redirect shortenerInstance
            }
            '*' {

                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: "shortener", action: 'show', id: shortenerInstance.id))

                respond shortenerInstance, [status: CREATED]
            }
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
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: this.controllerName, action: 'show', id: shortenerInstance.id, absolute: true))
                respond shortenerInstance, [status: OK]
            }
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
