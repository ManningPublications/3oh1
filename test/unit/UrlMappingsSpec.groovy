import com.manning.RedirectController
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import junit.framework.AssertionFailedError
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(UrlMappings)
@Mock(RedirectController)
class UrlMappingsSpec extends Specification {

    def "every url will be mapped to the redirect controller"() {

        expect:
        assertForwardUrlMapping('/abc', controller: 'redirect', action: 'index') { shortenerKey == 'abc' }
        assertForwardUrlMapping('/otherController', controller: 'redirect', action: 'index') { shortenerKey == 'otherController' }

    }

    def "only valid shortener keys are mapped to redirect controller"() {

        when:
        def longString = 'a' * 125
        def validShortenerKeys = ['abc', 'Abc', 'ABC', 'ab123', '123', longString]

        then:
        validShortenerKeys.each {
            assertForwardUrlMapping("/$it",  controller: 'redirect', action: 'index')
        }

    }

    def "invalid shortener keys are not mapped to redirect controller"() {

        when:
        def invalidShortenerKeys = ['a&23', 'ö@asd', '<<78hjh', '?asd=de']

        then:
        shouldFail {
            invalidShortenerKeys.each {
                assertForwardUrlMapping("/$it", controller: 'redirect', action: 'index')
            }
        }

    }

    @Ignore
    def "the root url will redirect to manning.com"() {

        // TODO: create unit test for redirection to manning.com
        expect:
        assertUrlMapping('/', redirect: 'http://www.manning.com')

    }

}