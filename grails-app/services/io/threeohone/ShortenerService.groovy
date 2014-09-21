package io.threeohone

import grails.orm.PagedResultList
import org.grails.plugins.quickSearch.QuickSearchService

class ShortenerService {

    static transactional = true

    def hashidsService

    def springSecurityService

    QuickSearchService quickSearchService
    def grailsApplication

    def createShortener(def params) {

        def shortener = new Shortener(params)

        shortener.userCreated = springSecurityService.currentUser

        if (shortener.save(flush: true)) {

            createShortenerKey(shortener)

            shortener.save(flush: true)
        }

        return shortener

    }

    public Shortener findActiveShortenerByKey(String shortenerKey) {
        def shortener = Shortener.findByShortenerKey(shortenerKey)

        if (shortener?.isActive()) {
            return shortener
        }

        return null
    }


    public def search(Map params) {

        def url = grailsApplication.config.grails.serverURL
        def port = grailsApplication.config.grails.serverPort
        def appName = grails.util.Metadata.current.'app.name'

        def wholeUrl = "${url}:${port}/${appName}/"

        def query = params.search ?: ''
        query -= wholeUrl

        def searchParams = [/*sort: 'destinationUrl', order: 'asc',*/ max: params.max, offset: params.offset]
        def searchProperties = [destinationUrl: 'destinationUrl',
                                shortenerKey  : 'shortenerKey'
        ]

        def customClosure = Shortener.getCustomClosureByValidity(params.validity)

        quickSearchService.search(domainClass: Shortener, searchParams: searchParams,
                searchProperties: searchProperties, customClosure: customClosure, query: query)

    }


    private void createShortenerKey(Shortener shortener) {
        shortener.shortenerKey = hashidsService.encrypt(shortener.id)
    }
}
