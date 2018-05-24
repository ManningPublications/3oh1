package io.threeohone

class ShortenerTagLib {

    static namespace = 'shortener'

    def shortUrl = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        out << g.createLink(absolute: 'true', uri: '/' + shortener.key)
    }

    def shortLink = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        if (shortener.isActive()) {
            out << g.link(absolute: true, uri: '/' + shortener.key) {
                shortUrl(shortener: shortener)
            }
        }
        else {
            out << shortUrl(shortener: shortener)
        }
    }


    def showWarningIfNotActive = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        if (!shortener.isActive()) {
            def messageKey = null

            out << '<div id="shortener-not-active-warning" class="alert alert-danger alert-dismissible" role="alert">'
            out << '<span class="fa fa-clock"></span> '

            if (!shortener.isStarted()) {
                messageKey = 'shortener.redirection.validFrom.disabled'
            }
            else if(shortener.isEnded()) {
                messageKey = 'shortener.redirection.validUntil.disabled'
            }

            if (messageKey) {
                out << g.message(code: messageKey)
            }

            out << '</div>'
        }



    }

    def prettyDestinationUrl = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        String destinationUrl = shortener.destinationUrl

        destinationUrl -= "http://"
        destinationUrl -= "https://"
        destinationUrl -= "www."


        def stringSizeLimit = 25

        if (destinationUrl.length() >= stringSizeLimit) {
            destinationUrl = destinationUrl.substring(0, stringSizeLimit) + '...'
        }

        if (attrs.link) {
            out << g.link(uri: shortener.destinationUrl) {
                destinationUrl
            }
        }
        else {
            out << destinationUrl
        }
    }



}
