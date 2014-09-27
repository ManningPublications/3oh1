package io.threeohone

import fm.jiecao.lib.Hashids
import grails.orm.PagedResultList
import org.grails.plugins.quickSearch.QuickSearchService


class ShortenerService {

    static transactional = true

    public static final String DEFAULT_SALT = "3oh1.io"


    def springSecurityService
    QuickSearchService quickSearchService
    def grailsApplication


    /**
     * finds a Shortener by a given shortenerKey if the key exists and the shortener is active
     * @param shortenerKey the key to search for
     * @return the shortner if found, otherwise null
     */
    Shortener findActiveShortenerByKey(String shortenerKey) {
        def shortener = Shortener.findByShortenerKey(shortenerKey)

        if (shortener?.isActive()) {
            return shortener
        }

        return null
    }

    public def search(Map params) {

        def url = grailsApplication.config.grails.serverURL
    
        def wholeUrl = "${url}/"
    
        def query = params.search ?: ''
        query -= wholeUrl
    
        def searchParams = [/*sort: 'destinationUrl', order: 'asc',*/ max: params.max, offset: params.offset]
        def searchProperties = [destinationUrl: 'destinationUrl',
                shortenerKey  : 'shortenerKey',
                username      : 'userCreated.username'
        ]
    
        def customClosure = Shortener.getCustomClosureByValidity(params.validity)
    
        quickSearchService.search(domainClass: Shortener, searchParams: searchParams,
                searchProperties: searchProperties, customClosure: customClosure, query: query)

    }


    /**
     * creates and persists a new shortener for the given parameters. A new shortnerKey will be generated
     * @param params the parameters for the shortener entry.
     *               The shortenerKey can not be set here (@see ShortenerService.importExistingShortener)
     * @return the (un)saved shortener
     */
    def createShortener(def params) {

        Shortener shortener = tryToSaveShortener(params)

        if (!shortener.hasErrors()) {
            createShortenerKey(shortener)
            shortener.save(flush: true)
        }

        return shortener
    }


    /**
     * creates and persists a new shortener for the given parameters. A given shortenerKey is required
     * @param params the parameters for the shortener entry.
     *               The shortenerKey has to be set here
     * @return the (un)saved shortener
     */
    def importExistingShortener(def params) {
        return tryToSaveShortener(params)
    }

    private Shortener tryToSaveShortener(params) {

        params.userCreated = springSecurityService.currentUser
        def shortener = new Shortener(params)
        shortener.save(flush: true)

        return shortener
    }



    private void createShortenerKey(Shortener shortener) {

        tryToSaveShortenerKeyForShortener(shortener, DEFAULT_SALT)

        while (hasUniqueError(shortener)) {
            tryToSaveShortenerKeyForShortener(shortener, randomSalt())
        }

    }



    private void tryToSaveShortenerKeyForShortener(Shortener shortener, String salt) {
        shortener.shortenerKey = new Hashids(salt).encode(shortener.id)
        shortener.save()
    }

    private boolean hasUniqueError(Shortener shortener) {
        return shortener.errors?.getFieldError("shortenerKey")?.codes?.contains("unique")
    }

    private String randomSalt() {
        (1..10).inject("") { a, b -> a += ('a'..'z')[new Random().nextFloat() * 26 as int] }.capitalize()
    }
}