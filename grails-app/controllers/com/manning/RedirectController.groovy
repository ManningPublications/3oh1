package com.manning

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured(['permitAll'])
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
