package com.manning

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import spock.lang.Specification

@TestFor(ShortenerTagLib)
class ShortenerTagLibSpec extends Specification {



    void "s:showValidity needs a bean attribute"() {

        when:
        applyTemplate('<s:showValidity />')

        then:
        thrown GrailsTagException
    }

    def "s:showValidity joins validFrom and validUntil with a dash"() {

        given:
        def now = new Date()
        def shortener = new Shortener(validFrom: now, validUntil: now + 1)
        when:
        def actualHtml = applyTemplate('<s:showValidity bean="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml.contains(' - ')
    }

    def "s:showValidity prints only the validFrom if no validUntil is set"() {

        given:
        messageSource.addMessage('shortener.validFrom.label', request.locale, 'validFrom')
        def now = new Date()
        def shortener = new Shortener(validFrom: now)
        when:
        def actualHtml = applyTemplate('<s:showValidity bean="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml.contains('validFrom')
    }


}
