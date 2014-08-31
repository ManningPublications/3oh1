import com.manning.Shortener

class BootStrap {

    def init = {

        25.times {
        new Shortener(
                shortenerKey: 'abc' + it,
                destinationUrl: 'http://www.twitter.com/' + it,
                validFrom: new Date(),
                validUntil: new Date() + it,
                userCreated: "Dummy User"
        ).save(failOnError: true)
        }


        environments {
            test {
                createTestFixtures()
            }
        }


    }

    private void createTestFixtures() {

        new Shortener(
                shortenerKey: 'httpsTwitterCom',
                destinationUrl: 'http://www.twitter.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: "Dummy User"
        ).save(failOnError: true)

        new Shortener(
                shortenerKey: 'httpGoogleCom',
                destinationUrl: 'http://www.google.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: "Dummy User"
        ).save(failOnError: true)

        new Shortener(
                shortenerKey: 'httpSpec',
                destinationUrl: 'http://www.w3.org/Protocols/rfc2616/rfc2616.html',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: "Dummy User"
        ).save(failOnError: true)


        new Shortener(
                shortenerKey: 'httpSpecViaHttps',
                destinationUrl: 'https://www.ietf.org/rfc/rfc2616.txt',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: "Dummy User"
        ).save(failOnError: true)


    }
}
