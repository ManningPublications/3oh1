package com.manning

class ShortenerTagLib {

    static namespace = 'shortener'

    def shortUrl = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        out << g.createLink(absolute:'true', uri:'/' + shortener.shortenerKey)
    }

    def shortLink = { attrs, body ->
        def shortener = attrs.shortener

        if (!shortener) throwTagError("a shortener has to be set")

        out << g.link(absolute: true, uri:'/' + shortener.shortenerKey) {
            shortUrl(shortener: shortener)
        }
    }
}
