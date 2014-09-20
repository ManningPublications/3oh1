package io.threeohone

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import spock.lang.Specification

@TestFor(ShortenerTagLib)
class ShortenerTagLibSpec extends Specification {



    void "shortener:shortUrl needs a shortener attribute"() {

        when:
        applyTemplate('<shortener:shortUrl />')

        then:
        thrown GrailsTagException
    }

    def "shortener:shortUrl returns the base url combines with the shortenerKey as a http url "() {

        given:
        def shortener = new Shortener(shortenerKey: "abc")
        when:
        def actualUrl = applyTemplate('<shortener:shortUrl shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualUrl == 'http://localhost:8080/abc'
    }

    void "shortener:shortLink needs a shortener attribute"() {

        when:
        applyTemplate('<shortener:shortLink />')

        then:
        thrown GrailsTagException
    }

    def "shortener:shortLink returns only the url without a link if the shortener is not active"() {

        given:
        def tomorrow = new Date() + 1
        def shortener = new Shortener(shortenerKey: "abc", validFrom: tomorrow)

        when:
        def actualUrl = applyTemplate('<shortener:shortLink shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualUrl == 'http://localhost:8080/abc'
    }





    void "shortener:showRedirectionValidityMessage needs a shortener attribute"() {

        when:
        applyTemplate('<shortener:shortUrl />')

        then:
        thrown GrailsTagException
    }

    def "shortener:showWarningIfNotActive returns a danger alert if the shortener is not started"() {

        given:
        messageSource.addMessage('shortener.redirection.validFrom.disabled', request.locale, 'validFromDisabled')
        def tomorrow = new Date() + 1
        def shortener = new Shortener(shortenerKey: "abc", validFrom: tomorrow)

        when:
        def actualHtml = applyTemplate('<shortener:showWarningIfNotActive shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml.contains "alert-danger"
        actualHtml.contains "validFromDisabled"
    }


    def "shortener:showWarningIfNotActive returns a danger alert if the shortener is ended"() {

        given:
        messageSource.addMessage('shortener.redirection.validUntil.disabled', request.locale, 'validUntilDisabled')
        def yesterday = new Date() - 1
        def shortener = new Shortener(shortenerKey: "abc", validFrom: new Date(), validUntil: yesterday)

        when:
        def actualHtml = applyTemplate('<shortener:showWarningIfNotActive shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml.contains "alert-danger"
        actualHtml.contains "validUntilDisabled"

    }

    def "shortener:showWarningIfNotActive returns nothing if shortener is active"() {

        given:
        def yesterday = new Date() - 1
        def shortener = new Shortener(shortenerKey: "abc", validFrom: yesterday)

        when:
        def actualHtml = applyTemplate('<shortener:showWarningIfNotActive shortener="${shortener}"/>', [shortener: shortener])

        then:
        !actualHtml

    }

    def "prettyDestinationUrl removes http:// from the url"() {

        given:
        def shortener = new Shortener(destinationUrl: 'http://google.com')
        when:
        def actualHtml = applyTemplate('<shortener:prettyDestinationUrl shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml == 'google.com'
    }

    def "prettyDestinationUrl removes www. from the url"() {

        given:
        def shortener = new Shortener(destinationUrl: 'www.google.com')
        when:
        def actualHtml = applyTemplate('<shortener:prettyDestinationUrl shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml == 'google.com'
    }


    def "prettyDestinationUrl cuts the url after 25 chars"() {

        given:
        def shortener = new Shortener(destinationUrl: ('a' * 25) + '.com')
        when:
        def actualHtml = applyTemplate('<shortener:prettyDestinationUrl shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualHtml == ('a' * 25) + '...'
    }


    def "prettyDestinationUrl creates can create a link to the correct url"() {

        given:
        def shortener = new Shortener(destinationUrl: 'www.google.com')
        when:
        def actualHtml = applyTemplate('<shortener:prettyDestinationUrl shortener="${shortener}" link="${true}"/>', [shortener: shortener, true: true])

        then:
        actualHtml.contains '<a href="www.google.com"'
    }


}
