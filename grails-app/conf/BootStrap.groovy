import com.manning.Shortener

class BootStrap {

    def init = {
        new Shortener(
                shortenerKey: 'abc',
                destinationUrl: 'http://www.google.com',
                validFrom: new Date(),
                validUntil: new Date() + 1,
                userCreated: "Dummy User"
        ).save(failOnError:true)
    }
}
