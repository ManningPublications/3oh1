import grails.converters.JSON
import io.threeohone.ClientInformation
import io.threeohone.RedirectLog
import io.threeohone.Shortener
import io.threeohone.security.Role
import io.threeohone.security.User
import io.threeohone.security.UserRole

class BootStrap {

    def init = {

        registerCustomJSONMarshallers()

        createAdmin()

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

        JSON.registerObjectMarshaller(Shortener) { Shortener shortener ->
            [
                shortenerKey: shortener.shortenerKey,
                destinationUrl: shortener.destinationUrl,
                userCreated: shortener.userCreated.username,
                dateCreated: shortener.dateCreated,
                validFrom: shortener.validFrom,
                validUntil: shortener.validUntil
            ]

        }

        JSON.registerObjectMarshaller(User) { User user ->
             [ username: user.username ]
        }


    }

    private void createAdmin() {

        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)

        def adminUser = User.findByUsername('admin') ?: new User(username: 'admin', password: 'admin', enabled: true).save(failOnError: true)
        if (!adminUser.authorities.contains(adminRole)) {
            UserRole.create adminUser, adminRole
        }


        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def user = User.findByUsername('user') ?: new User(username: 'user', password: 'user', enabled: true).save(failOnError: true)
        if (!user.authorities.contains(userRole)) {
            UserRole.create user, userRole
        }

        def apiUser = User.findByUsername('apiUser') ?: new User(username: 'apiUser', password: 'apiUser', enabled: true).save(failOnError: true)
        if (!apiUser.authorities.contains(userRole)) {
            UserRole.create user, userRole
        }

    }

    private void createTestShorteners() {

        def user = User.findByUsername('user')

        25.times {
            createActiveShortenerBySimpleName('test' + it)
        }

        Shortener.findOrSaveWhere(
                shortenerKey: 'httpsTwitterCom',
                destinationUrl: 'https://www.twitter.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )

        Shortener.findOrSaveWhere(
                shortenerKey: 'httpGoogleCom',
                destinationUrl: 'http://www.google.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )

        Shortener.findOrSaveWhere(
                shortenerKey: 'httpSpec',
                destinationUrl: 'http://www.w3.org/Protocols/rfc2616/rfc2616.html',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )


        Shortener.findOrSaveWhere(
                shortenerKey: 'httpSpecViaHttps',
                destinationUrl: 'https://www.ietf.org/rfc/rfc2616.txt',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )


    }

    def createActiveShortenerBySimpleName(String s) {
        def user = User.findByUsername('user')
        Shortener.findOrSaveWhere(
                shortenerKey: s,
                destinationUrl: 'http://www.' + s + '.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
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
                        clientIp: "192.168.0." + (it + 1),
                        referer: "http://www.google.com",

                        clientInformation: new ClientInformation(clientInformationAttributes[(it * it2) % 6])
                ).save(failOnError: true)

                log.dateCreated = new Date() - (it * it2)
            }

        }

    }

}
