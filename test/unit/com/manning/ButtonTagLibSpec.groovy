package com.manning

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import spock.lang.Specification

@TestFor(ButtonTagLib)
class ButtonTagLibSpec extends Specification {
    private XmlSlurper slurper

    void setup() {
        slurper = new XmlSlurper()
    }

    void "s:buttonWithActiveState is active when the property is 'future' and the params.validity is set to future"() {

        given:
        params.validity = 'future'

        when:
        def actualHtml = applyTemplate('<s:shortenerValidityButton property="future" />')

        then:
        actualHtml.contains('active')
    }

    void "s:buttonWithActiveState is not active when the property is not the validity param"() {

        given:
        params.validity = 'expired'

        when:
        def actualHtml = slurper.parseText(
                applyTemplate('<s:shortenerValidityButton property="future" />')
        )

        then:
        !actualHtml.text().contains('active')
    }


    void "s:buttonWithActiveState displays the i18n content of a given key"() {

        given:
        messageSource.addMessage('shortener.future.label', request.locale, 'hello')

        when:
        def actualHtml = slurper.parseText(
                applyTemplate('<s:shortenerValidityButton property="future" />')
        )

        then:
        actualHtml.text().contains('hello')
    }

    void "s:buttonWithActiveState links to the same page with the property as the activity parameter"() {

        when:
        def actualHtml = slurper.parseText(
                applyTemplate('<s:shortenerValidityButton property="future" />')
        )

        then:
        actualHtml.@href.text().contains '?validity=future'
    }


    void "s:buttonWithActiveState needs a property attribute"() {

        when:
        applyTemplate('<s:shortenerValidityButton />')

        then:
        thrown GrailsTagException
    }

    void "s:buttonWithActiveState needs a valid property attribute"() {

        when:
        applyTemplate('<s:shortenerValidityButton property="future" label="invalid.key" />')

        then:
        notThrown GrailsTagException

        when:
        applyTemplate('<s:shortenerValidityButton property="active" label="invalid.key" />')

        then:
        notThrown GrailsTagException

        when:
        applyTemplate('<s:shortenerValidityButton property="expired" label="invalid.key" />')

        then:
        notThrown GrailsTagException



        when:
        applyTemplate('<s:shortenerValidityButton property="notValidAttribute" label="invalid.key" />')

        then:
        thrown GrailsTagException

    }


}
