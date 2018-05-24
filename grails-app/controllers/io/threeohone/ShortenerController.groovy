package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import io.threeohone.security.User
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.*

@Secured(['isAuthenticated()'])
class ShortenerController {

    static responseFormats = ['html', 'json']

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]


    StatisticsService statisticsService

    ShortenerService shortenerService

    ShortenerSearchService shortenerSearchService

    def index(Integer max) {

        params.max = Math.min(max ?: 10, 100)
        params.offset = params.offset ?: 0
        params.sort = params.sort ?: 'key'
        params.order = params.order ?: 'asc'


        def validity = Shortener.getValidityByString(params.validity)


        def searchQuery = params.search ?: ''
        searchQuery = cutServerUrlIfNecessary(searchQuery)

        def shortenerList

        def user = User.findByUsername(params.userId)

        if (user) {
            shortenerList = shortenerSearchService.searchByUser(searchQuery, validity, user, params)
        }
        else {
            shortenerList = shortenerSearchService.search(searchQuery, validity, params)
        }


        if (isOnlyOneSearchResult(shortenerList)) {
            redirect(resource: "shortener", action: 'show', id: shortenerList.first().key)
            return
        }

        def shortenerInstanceCount = shortenerList.size()
        response.setHeader("total-count", Integer.toString(shortenerInstanceCount))

        respond shortenerList, model: [shortenerInstanceList: shortenerList, shortenerInstanceCount: shortenerInstanceCount, user: user]
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

    private boolean isOnlyOneSearchResult(shortenerList) {
        shortenerList.size() == 1 && params.search
    }


    def show() {
        Shortener shortenerInstance = Shortener.findByKey(params.id)

        def result = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)
        def redirectCountersPerOperatingSystem = statisticsService.getRedirectCounterGroupedBy(shortenerInstance, 'operatingSystem')
        def redirectCountersPerBrowser = statisticsService.getRedirectCounterGroupedBy(shortenerInstance, 'browserName')

        def totalNumberOfRedirectsPerMonth = [monthNames: [], redirectCounters: []]
        totalNumberOfRedirectsPerMonth.monthNames = result.collect { "${it.month} / ${it.year}" }
        totalNumberOfRedirectsPerMonth.redirectCounters = result.collect { it.redirectCounter }

        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()


        respond shortenerInstance, model: [
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                redirectCounter: redirectCounter,
                redirectCountersPerOperatingSystem: redirectCountersPerOperatingSystem,
                redirectCountersPerBrowser: redirectCountersPerBrowser
        ]
    }

    def create() {
        respond new Shortener(params)
    }

    def save(ShortenerCreateCommand createCommand) {


        Shortener shortenerInstance

        if (createCommand.key) {
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
                redirect(resource: "shortener", action: 'show', id: shortenerInstance.key)
            }
            '*' {

                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: "shortener", action: 'show', id: shortenerInstance.key))

                respond shortenerInstance, [status: CREATED]
            }
        }
    }

    def edit() {
        Shortener shortenerInstance = Shortener.findByKey(params.id)
        respond shortenerInstance
    }

    @Transactional
    def update() {
        Shortener shortenerInstance = Shortener.findByKey(params.id)

        if (shortenerInstance == null) {
            notFound()
            return
        }

        bindData(shortenerInstance, params)

        shortenerInstance.validate()

        if (shortenerInstance.hasErrors()) {
            respond shortenerInstance.errors, view: 'edit'
            return
        }

        shortenerInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [shortener.shortUrl(shortener: shortenerInstance)])
                redirect(resource: "shortener", action: 'show', id: shortenerInstance.key)
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        g.createLink(
                                resource: this.controllerName, action: 'show', id: shortenerInstance.key, absolute: true))
                respond shortenerInstance, [status: OK]
            }
        }
    }

    @Transactional
    def delete() {
        Shortener shortenerInstance = Shortener.findByKey(params.id)

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
    String key
    String destinationUrl
    Date validFrom
    Date validUntil


}
