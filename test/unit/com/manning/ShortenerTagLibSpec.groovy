package com.manning

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import spock.lang.Specification

@TestFor(ShortenerTagLib)
class ShortenerTagLibSpec extends Specification {



    void "shortener:shortUrl needs a bean attribute"() {

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

    void "shortener:shortLink needs a bean attribute"() {

        when:
        applyTemplate('<shortener:shortLink />')

        then:
        thrown GrailsTagException
    }

    def "shortener:shortLink returns the base url combines with the shortenerKey as a html link "() {

        given:
        def shortener = new Shortener(shortenerKey: "abc")
        when:
        def actualUrl = applyTemplate('<shortener:shortLink shortener="${shortener}"/>', [shortener: shortener])

        then:
        actualUrl == '<a href="http://localhost:8080/abc">http://localhost:8080/abc</a>'
    }


}
