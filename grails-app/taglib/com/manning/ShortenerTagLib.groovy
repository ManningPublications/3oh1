package com.manning

class ShortenerTagLib {

    static namespace = 's'

    def showValidity = { attrs, body ->
        def shortener = attrs.bean

        if (!shortener) throwTagError("a bean has to be set")

        if (shortener.validUntil) {
            out << g.formatDate(date: shortener.validFrom, style: 'SHORT')
            out << ' - '
            out << g.formatDate(date: shortener.validUntil, style: 'SHORT')
        }
        else {
            out << g.message(code: 'shortener.validFrom.label')
            out << ': '
            out << g.formatDate(date: shortener.validFrom, style: 'SHORT')
        }
    }
}
