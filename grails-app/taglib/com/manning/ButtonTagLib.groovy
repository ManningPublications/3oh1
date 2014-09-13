package com.manning

class ButtonTagLib {

    static namespace = 's'

    def shortenerValidityButton = { attrs, body ->

        if (!attrs.property) throwTagError("a property has to be set")

        def allowedProperty = ['expired', 'active', 'future']

        if (!allowedProperty.contains(attrs.property)) {
            throwTagError('property has to one of ' + allowedProperty + ', but was ' + attrs.property)
        }


        def classes = ['btn', 'btn-default']

        if (params.validity == attrs.property) {
            classes << 'active'
        }

        out << g.link( class: classes.join(" "), params: [validity: attrs.property, search:params.search] ) {
            g.message(code: "shortener.${attrs.property}.label")
        }
    }
}
