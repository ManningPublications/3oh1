package com.manning

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*


@TestFor(RedirectController)
class RedirectControllerSpec extends Specification {


    def redirectFinderMock

    def setup() {
        redirectFinderMock = Mock(RedirectFinderService)
        controller.redirectFinderService = redirectFinderMock
    }

    void 'for a valid shortener entry a redirect should occur to the destination url'() {

        when:
        params.shortenerKey = 'abc'
        controller.index()

        then:
        response.redirectedUrl == 'http://www.example.com'
        response.status == MOVED_PERMANENTLY.value()

        and: 'the mock was asked once and returned the correct value'
        1 * redirectFinderMock.findRedirectionUrlForKey('abc') >> 'http://www.example.com'

    }

    def 'an invalid shortener get a 404 and an error page'() {

        when:
        params.shortenerKey = 'abc'
        controller.index()

        then:
        response.status == NOT_FOUND.value()
        view == '/notFound'

        and: 'the mock was asked once and returned the correct value'
        1 * redirectFinderMock.findRedirectionUrlForKey('abc') >> null
    }



}
