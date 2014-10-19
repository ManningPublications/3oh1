package io.threeohone

import fm.jiecao.lib.Hashids
import io.threeohone.security.User


class ShortenerService {

    static transactional = true

    public static final String DEFAULT_SALT = "3oh1.io"

    def springSecurityService

    /**
     * finds a Shortener by a given shortenerKey if the key exists and the shortener is active
     * @param shortenerKey the key to search for
     * @return the shortner if found, otherwise null
     */
    Shortener findActiveShortenerByKey(String shortenerKey) {
        def shortener = Shortener.findByShortenerKey(shortenerKey)

        if (shortener?.isActive()) {
            return shortener
        }

        return null
    }



    /**
     * creates and persists a new shortener for the given parameters. A new shortnerKey will be generated
     * @param createCommand the parameters for the shortener entry.
     *               The shortenerKey can not be set here (@see ShortenerService.importExistingShortener)
     * @return the (un)saved shortener
     */
    def createShortener(ShortenerCreateCommand createCommand) {

        Shortener shortener = tryToSaveShortener(createCommand)

        if (!shortener.hasErrors()) {
            createShortenerKey(shortener)
            shortener.save(flush: true)
        }

        return shortener
    }

    /**
     * creates and persists a new shortener for the given parameters. A given shortenerKey is required
     * @param createCommand the parameters for the shortener entry.
     *               The shortenerKey has to be set here
     * @return the (un)saved shortener
     */
    def importExistingShortener(ShortenerCreateCommand createCommand) {
        return tryToSaveShortener(createCommand)
    }


    private Shortener tryToSaveShortener(ShortenerCreateCommand createCommand) {


        def shortenerParams = [
                userCreated: determineUserForShortener(createCommand),
                destinationUrl: createCommand.destinationUrl,
                shortenerKey: createCommand.shortenerKey,
                validFrom: createCommand.validFrom ?: new Date(),
                validUntil: createCommand.validUntil
        ]

        def shortener = new Shortener(shortenerParams)
        shortener.save(flush: true)

        return shortener
    }

    private User determineUserForShortener(params) {
        def assignedUser = User.findByUsername(params.userCreated)

        return assignedUser ?: springSecurityService.currentUser

    }


    private void createShortenerKey(Shortener shortener) {

        tryToSaveShortenerKeyForShortener(shortener, DEFAULT_SALT)

        while (hasUniqueError(shortener)) {
            tryToSaveShortenerKeyForShortener(shortener, randomSalt())
        }

    }


    private void tryToSaveShortenerKeyForShortener(Shortener shortener, String salt) {
        shortener.shortenerKey = new Hashids(salt).encode(shortener.id)
        shortener.save()
    }


    private boolean hasUniqueError(Shortener shortener) {
        return shortener.errors?.getFieldError("shortenerKey")?.codes?.contains("unique")
    }


    private String randomSalt() {
        (1..10).inject("") { a, b -> a += ('a'..'z')[new Random().nextFloat() * 26 as int] }.capitalize()
    }
}
