package io.threeohone

import io.threeohone.security.User

class Shortener {


    static keyBlacklist = ["shorteners", "api", "users", "statistics", "docs"]

    enum Validity {
        ACTIVE, EXPIRED, FUTURE
    }

    static Shortener.Validity getValidityByString(String validity) {

        def result = null

        if (validity) {
            def lowerCaseValidity = validity.toLowerCase()

            switch (lowerCaseValidity) {
                case 'active': result = Shortener.Validity.ACTIVE; break;
                case 'expired': result = Shortener.Validity.EXPIRED; break;
                case 'future': result = Shortener.Validity.FUTURE; break;

            }
        }

        return result
    }

    String destinationUrl
    String key

    Date validFrom
    Date validUntil

    Date dateCreated
    Date lastUpdated


    Long userId

    static transients  = ["userCreated"]
    static constraints = {
        /*
          nullable has to be true in order to create a temp shortener (with an id). This shortener is used directly
          after creation for generating the key from the id, which is then persisted.

          The custom validator is for blacklist validation.
         */
        key unique: true, nullable: true, validator: { val, obj, errors ->

            keyBlacklist.each {
                if (it == val) errors.rejectValue('key', 'unique')
            }

        }
        destinationUrl url: true, nullable: false
        validUntil nullable: true
    }

    static mapping = {
        key index: 'key_idx', column: 'shortener_key'
        destinationUrl type: 'text'
    }

    boolean isStarted() {
        validFrom.before(new Date())
    }

    boolean isEnded() {
        validUntil?.before(new Date())
    }

    boolean isActive() {
        isStarted() && !isEnded()
    }

    User getUserCreated() {
        userId ? User.withTransaction{User.get(userId)} : null
    }

    void setUserCreated(User user) {
        if (user) {
            userId  = user.id
        }
    }
}
