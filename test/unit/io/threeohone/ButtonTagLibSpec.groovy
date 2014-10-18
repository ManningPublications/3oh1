package io.threeohone

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import spock.lang.Specification

@TestFor(ButtonTagLib)
class ButtonTagLibSpec extends Specification {
    private XmlSlurper slurper

    void setup() {
        slurper = new XmlSlurper()
    }


    void "s:buttonWithActiveState is active when the property is the same as params.validity"() {

        given:
        params.validity = 'future'

        when:
        def actualHtml = applyTemplate('<s:shortenerValidityButton property="future" />')

        then:
        actualHtml.contains('active')
    }

    void "s:buttonWithActiveState creates a link for a active that removes the filter"() {

        given:
        params.validity = 'future'

        when:
        def actualHtml = applyTemplate('<s:shortenerValidityButton property="future" />')

        then: "the validity attribute is not set in the link anymore"
        !actualHtml.contains("validity=future")
    }

    void "s:buttonWithActiveState adds the search attribute only if there is already search string given"() {

        given:
        params.validity = 'future'
        params.search = null

        when:
        def actualHtml = applyTemplate('<s:shortenerValidityButton property="future" />')

        then: "the search attribute is not send"
        !actualHtml.contains("search=")


        when:
        params.search = "searchString"
        actualHtml = applyTemplate('<s:shortenerValidityButton property="future" />')

        then: "the search attribute is send"
        actualHtml.contains("search=searchString")
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
