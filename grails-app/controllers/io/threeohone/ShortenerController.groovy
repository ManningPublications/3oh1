package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

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
        params.offset = 0


        def validity = Shortener.getValidityByString(params.validity)

        def shortenerList = shortenerService.search(params.search, validity, params.max, params.offset)

        respond shortenerList, model: [shortenerInstanceCount: shortenerList.size()]
    }


    def show(Shortener shortenerInstance) {


        def result = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)

        def totalNumberOfRedirectsPerMonth = [monthNames: [], redirectCounters: []]
        totalNumberOfRedirectsPerMonth.monthNames = result.collect { "${it.month} / ${it.year}" }
        totalNumberOfRedirectsPerMonth.redirectCounters = result.collect { it.redirectCounter }

        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()


        respond shortenerInstance, model: [
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                redirectCounter: redirectCounter
        ]
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
