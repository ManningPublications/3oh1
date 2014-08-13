package com.manning

class RedirectController {

    def index() {

        def key = params.shortenerKey
        def destinationUrl = null

        if (key) {
            destinationUrl = Shortener.findByShortenerKey(key).destinationUrl
        }
        else {
            destinationUrl = 'http://www.manning.com'
        }

        redirect url: destinationUrl, permanent: true
    }
}
