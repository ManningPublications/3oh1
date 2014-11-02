package io.threeohone

class ButtonTagLib {

    static namespace = 's'

    def shortenerValidityButton = { attrs, body ->

        if (!attrs.property) throwTagError("a property has to be set")

        def allowedProperty = ['expired', 'active', 'future']

        if (!allowedProperty.contains(attrs.property)) {
            throwTagError('property has to one of ' + allowedProperty + ', but was ' + attrs.property)
        }


        def classes = ['btn', 'btn-default']


        def linkParams = [:]

        if (params.validity != attrs.property) linkParams.validity = attrs.property
        if (params.search) linkParams.search = params.search
        if (params.userId) linkParams.userId = params.userId

        if (params.validity == attrs.property) {
            classes << 'active'
        }

        out << g.link(
                class: classes.join(" "),
                params: linkParams,
                elementId: attrs.property) {
            g.message(code: "shortener.${attrs.property}.label")
        }


    }
}
