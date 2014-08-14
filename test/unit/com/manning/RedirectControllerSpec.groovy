package com.manning

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Ignore
import spock.lang.Specification


@TestFor(RedirectController)
class RedirectControllerSpec extends Specification {


    public static final int MOVED_PERMANENTLY = 301
    public static final int NOT_FOUND = 404

    def redirectFinderMock

    def setup() {
        redirectFinderMock = Mock(RedirectFinderService)
        controller.redirectFinderService = redirectFinderMock
    }

    void 'if no shortenerKey is given a redirect to manning.com will be executed'() {

        when:
        params.shortenerKey = null
        controller.index()

        then:
        response.redirectedUrl == 'http://www.manning.com'
        response.status == MOVED_PERMANENTLY
    }


    void 'for a valid shortener entry a redirect should occur to the destination url'() {

        when:
        params.shortenerKey = 'abc'
        controller.index()

        then:
        response.redirectedUrl == 'http://www.example.com'
        response.status == MOVED_PERMANENTLY

        and: 'the mock was asked once and returned the correct value'
        1 * redirectFinderMock.findRedirectionUrlForKey('abc') >> 'http://www.example.com'

    }

    def 'a shortener that has no destination url will not be redirected'() {

        // TODO: is 404 ok (perhaps with error message) or do go a redirect to manning.com

        when:
        params.shortenerKey = 'abc'
        controller.index()

        then:
        response.status == NOT_FOUND

        and: 'the mock was asked once and returned the correct value'
        1 * redirectFinderMock.findRedirectionUrlForKey('abc') >> null
    }

    @Ignore
    def 'an invalid shortenerKey displays an error message'() {

        // TODO: does there have to be an error message (a 404 page) or is this just a redirection to manning.com?

        when:
        true

        then:
        false
    }


}
