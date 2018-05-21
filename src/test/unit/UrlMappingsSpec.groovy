import io.threeohone.RedirectController
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(UrlMappings)
@Mock(RedirectController)
class UrlMappingsSpec extends Specification {

    def "every url will be mapped to the redirect controller"() {

        expect:
        assertForwardUrlMapping('/abc', controller: 'redirect', action: 'index') { key == 'abc' }
        assertForwardUrlMapping('/otherController', controller: 'redirect', action: 'index') { key == 'otherController' }

    }

    def "only valid shortener keys are mapped to redirect controller"() {

        when:
        def longString = 'a' * 125
        def validKeys = ['abc', 'Abc', 'ABC', 'ab123', '123', longString]

        then:
        validKeys.each {
            assertForwardUrlMapping("/$it",  controller: 'redirect', action: 'index')
        }

    }

    def "invalid shortener keys are not mapped to redirect controller"() {

        when:
        def invalidKeys = ['a&23', 'ö@asd', '<<78hjh', '?asd=de']

        then:
        shouldFail {
            invalidKeys.each {
                assertForwardUrlMapping("/$it", controller: 'redirect', action: 'index')
            }
        }

    }

    @Ignore
    def "the root url will redirect to 3oh1.com"() {

        // TODO: create unit test for redirection to 3oh1.com
        expect:
        assertUrlMapping('/', redirect: 'http://www.3oh1.com')

    }

}