package m3oh1

import grails.converters.JSON
import io.threeohone.ClientInformation
import io.threeohone.RedirectLog
import io.threeohone.Shortener
import io.threeohone.security.User

class BootStrap {

    def init = {

        registerCustomJSONMarshallers()

        environments {
            test {
                createTestShorteners()
            }

            development {
                createTestShorteners()
                createLastRedirects()
            }

            production {

            }
        }


    }


    def registerCustomJSONMarshallers() {
        JSON.registerObjectMarshaller(Date) { it?.format("yyyy-MM-dd'T'HH:mm:ssZ") }
        JSON.registerObjectMarshaller(Shortener) { Shortener shortener ->
            [
                key: shortener.key,
                userId: shortener.userId,
                destinationUrl: shortener.destinationUrl,
                userCreated: shortener.userCreated,
                dateCreated: shortener.dateCreated,
                validFrom: shortener.validFrom,
                validUntil: shortener.validUntil
            ]

        }

        JSON.registerObjectMarshaller(User) { User user ->
             [ username: user.username ]
        }


    }

    private void createTestShorteners() {

        def user = User.findByUsername('api_user')

        25.times {
            createActiveShortenerBySimpleName('api_user' + it)
        }

        Shortener.findOrSaveWhere(
                key: 'httpsTwitterCom',
                destinationUrl: 'https://www.twitter.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userId: user.id
        )

        Shortener.findOrSaveWhere(
                key: 'httpGoogleCom',
                destinationUrl: 'http://www.google.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userId: user.id
        )

        Shortener.findOrSaveWhere(
                key: 'httpSpec',
                destinationUrl: 'http://www.w3.org/Protocols/rfc2616/rfc2616.html',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userId: user.id
        )


        Shortener.findOrSaveWhere(
                key: 'httpSpecViaHttps',
                destinationUrl: 'https://www.ietf.org/rfc/rfc2616.txt',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userId: user.id
        )


    }

    def createActiveShortenerBySimpleName(String s) {
        def user = User.findByUsername('api_user')
        Shortener.findOrSaveWhere(
                key: s,
                destinationUrl: 'http://www.' + s + '.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userId: user.id
        )
    }

    private void createLastRedirects() {

        def clientInformationAttributes = [
            [
                browserVersion: "26",
                browserName: "Firefox",
                operatingSystem: "Windows 7"
            ],[
                browserVersion: "9.0",
                browserName: "Internet Explorer",
                operatingSystem: "Windows 8"
            ],[
                browserVersion: "24",
                browserName: "Firefox",
                operatingSystem: "Ubuntu 14.04"
            ],[
                browserVersion: "38",
                browserName: "Chrome",
                operatingSystem: "Windows 7"
            ],[
                browserVersion: "35",
                browserName: "Chrome",
                operatingSystem: "Mac OS X"
            ],[
                browserVersion: "38",
                browserName: "Chrome",
                operatingSystem: "Ubuntu 12.04"
            ],
        ]

        100.times {

            def shortener = Shortener.get((it % 4) + 1)

            it.times { it2 ->
                def log = new RedirectLog(
                        shortener: shortener,
                        referer: "http://www.google.com",

                        clientInformation: new ClientInformation(clientInformationAttributes[(it * it2) % 6])
                ).save(failOnError: true)

                log.dateCreated = new Date() - (it * it2)
            }

        }

    }

}
