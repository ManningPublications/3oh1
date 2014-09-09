import com.manning.Shortener
import com.manning.security.Role
import com.manning.security.User
import com.manning.security.UserRole

class BootStrap {

    def springSecurityService

    def init = {

        environments {

            createAdmin()

            /*
            development {


                25.times {
                    new Shortener(
                            shortenerKey: 'abc' + it,
                            destinationUrl: 'http://www.twitter.com/' + it,
                            validFrom: new Date(),
                            validUntil: new Date() + it,
                            userCreated: "Dummy User"
                    ).save(failOnError: true)
                }

            }
            */

            //test {

                createTestFixtures()

            //}
        }


    }

    private void createAdmin() {

        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)

        def adminUser = User.findByUsername('admin') ?: new User( username: 'admin', password: 'admin', enabled: true).save(failOnError: true)
        if (!adminUser.authorities.contains(adminRole)) { UserRole.create adminUser, adminRole }


        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def user = User.findByUsername('user') ?: new User( username: 'user', password: 'user', enabled: true ).save(failOnError: true)
        if (!user.authorities.contains(userRole)) { UserRole.create user, userRole }

    }

    private void createTestFixtures() {

        def user = User.findByUsername('user')

        Shortener.findOrSaveWhere (
                shortenerKey: 'httpsTwitterCom',
                destinationUrl: 'http://www.twitter.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )

        Shortener.findOrSaveWhere (
                shortenerKey: 'httpGoogleCom',
                destinationUrl: 'http://www.google.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )

        Shortener.findOrSaveWhere (
                shortenerKey: 'httpSpec',
                destinationUrl: 'http://www.w3.org/Protocols/rfc2616/rfc2616.html',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )


        Shortener.findOrSaveWhere (
                shortenerKey: 'httpSpecViaHttps',
                destinationUrl: 'https://www.ietf.org/rfc/rfc2616.txt',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: user
        )


    }
}
