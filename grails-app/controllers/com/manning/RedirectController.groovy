package com.manning

import org.springframework.http.HttpStatus

class RedirectController {

    def redirectFinderService

    def index() {

        def key = params.shortenerKey
        def destinationUrl = null

        if (key) {
            destinationUrl = redirectFinderService.findRedirectionUrlForKey(key)
        }
        else {
            destinationUrl = 'http://www.manning.com'
        }

        if (!destinationUrl) {
            render status: HttpStatus.NOT_FOUND, view: '/error'
        }
        else {
            redirect url: destinationUrl, permanent: true
        }

    }
}
