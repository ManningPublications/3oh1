package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import io.threeohone.security.User
import org.codehaus.groovy.grails.web.servlet.HttpHeaders

import static org.springframework.http.HttpStatus.*

@Secured(['isAuthenticated()'])
class ShortenerController {

    static responseFormats = ['html', 'json']

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]


    StatisticsService statisticsService

    ShortenerService shortenerService

    ShortenerSearchService shortenerSearchService

    def grailsApplication

    def index(Integer max) {

        params.max = Math.min(max ?: 10, 100)
        params.offset = params.offset ?: 0
        params.sort = params.sort ?: 'destinationUrl'
        params.order = params.order ?: 'asc'
        def searchString = params.search ?: ''

        def validity = Shortener.getValidityByString(params.validity)

        searchString = cutServerUrlIfNecessary(searchString)


        def shortenerList = shortenerSearchService.search(searchString, validity, params.max, params.offset, params.sort, params.order)


        if (isOnlyOneSearchResult(shortenerList)) {
            redirect shortenerList.first()
            return
        }

        respond shortenerList, model: [shortenerInstanceCount: shortenerList.getTotalCount()]
    }

    private String cutServerUrlIfNecessary(searchString) {

        def serverUrl = getPotentialServerUrl()

        if (serverUrl && searchString.contains(serverUrl)) {
            return searchString.split("/").last()
        } else {
            return searchString
        }

    }

    private String getPotentialServerUrl() {

        // there is an entry in the config.groovy (most production systems)
        if (grailsApplication.config.grails?.serverURL instanceof String) {
            return grailsApplication.config.grails?.serverURL
        }

        // else the http header could be read
        else {
            return request.getHeader("Host")
        }
    }

    private boolean isOnlyOneSearchResult(List<Shortener> shortenerList) {
        shortenerList.size() == 1 && params.search
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

    def save(ShortenerCreateCommand createCommand) {


        Shortener shortenerInstance

        if (createCommand.shortenerKey) {
            shortenerInstance = shortenerService.importExistingShortener(createCommand)
        } else {
            shortenerInstance = shortenerService.createShortener(createCommand)
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

/**
 * this command objects differs a little from the shortener domain object.
 * The userCreated attribute is a String that holds the username.
 * To get a valid binding in the save method of the controller we use this class
 */
class ShortenerCreateCommand {

    String userCreated
    String shortenerKey
    String destinationUrl
    Date validFrom
    Date validUntil


}
