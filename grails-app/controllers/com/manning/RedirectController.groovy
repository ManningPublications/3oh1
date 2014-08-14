package com.manning

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
            render status: 404
        }
        else {
            redirect url: destinationUrl, permanent: true
        }

    }
}
