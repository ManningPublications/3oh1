import io.threeohone.RedirectLog
import io.threeohone.Shortener
import io.threeohone.security.Role
import io.threeohone.security.User
import io.threeohone.security.UserRole

class BootStrap {

    def init = {

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
                createLastRedirects()
            }
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
                destinationUrl: 'http://www.twitter.com',
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

        50.times {

            def shortener = Shortener.get((it % 4) + 1)

            it.times { it2 ->
                def log = new RedirectLog(
                        shortener: shortener,
                        userAgent: "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                        clientIp: "192.168.0." + (it + 1),
                        referer: "http://www.google.com"
                ).save()

                log.dateCreated = new Date() - (it * it2)
            }

        }

    }

}
