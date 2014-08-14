package com.manning

import org.springframework.http.HttpStatus

class RedirectController {

    def redirectFinderService

    def index() {

        def key = params.shortenerKey
        def destinationUrl

        if (key) {
            destinationUrl = redirectFinderService.findRedirectionUrlForKey(key)
        }

        if (!destinationUrl) {
            render status: HttpStatus.NOT_FOUND, view: '/notFound'
        }
        else {
            redirect url: destinationUrl, permanent: true
        }

    }
}
