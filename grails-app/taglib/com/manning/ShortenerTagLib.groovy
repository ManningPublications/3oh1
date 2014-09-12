package com.manning

class ShortenerTagLib {

    static namespace = 'shortener'

    def shortUrl = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        out << g.createLink(absolute: 'true', uri: '/' + shortener.shortenerKey)
    }

    def shortLink = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        if (shortener.isActive()) {
            out << g.link(absolute: true, uri: '/' + shortener.shortenerKey) {
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
            out << '<span class="glyphicon glyphicon-time"></span> '

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



}
