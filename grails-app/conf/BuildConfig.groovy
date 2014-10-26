grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
        // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
        //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

        // configure settings for the test-app JVM, uses the daemon by default
        test   : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon: true],
        // configure settings for the run-app JVM
        run    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
        // configure settings for the run-war JVM
        war    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
        // configure settings for the Console UI JVM
        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global") { }
    log "error"
    checksums true
    legacyResolve false

    def gebVersion = "0.9.3"
    def seleniumVersion = "2.43.1"

    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        test "org.grails:grails-datastore-test-support:1.0-grails-2.4"

        test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"
        test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"

        test "org.gebish:geb-spock:$gebVersion"

        test("com.github.detro.ghostdriver:phantomjsdriver:1.1.0") {
            transitive = false
        }

    }

    plugins {
        // grails default plugins
        build ":tomcat:7.0.55"
        runtime ":hibernate4:4.3.5.5"
        runtime ":database-migration:1.4.0"
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.6'

        // how to deliver static assets to the client
        compile ":asset-pipeline:1.9.9"
        compile ":less-asset-pipeline:1.10.0"

        // for a good looking frontend
        compile ":twitter-bootstrap:3.2.0.2"
        runtime ":jquery:1.11.1"

        // stuff for functional testing
        test ":geb:$gebVersion"
        compile ":rest-client-builder:2.0.3"

        // fields plugin makes creating forms a breeze
        compile ":fields:1.4"

        compile ':spring-security-core:2.0-RC4'

        compile ":excel-export:0.2.1"


    }
}
