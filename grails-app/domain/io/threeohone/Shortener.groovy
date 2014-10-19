package io.threeohone

import io.threeohone.security.User

class Shortener {


    static shortenerKeyBlacklist = ["shorteners", "api", "users", "statistics", "docs"]

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
    String shortenerKey

    Date validFrom
    Date validUntil

    Date dateCreated
    Date lastUpdated


    User userCreated


    static constraints = {
        /*
          nullable has to be true in order to create a temp shortener (with an id). This shortener is used directly
          after creation for generating the shortenerKey from the id, which is then persisted.

          The custom validator is for blacklist validation.
         */
        shortenerKey unique: true, nullable: true, validator: { val, obj, errors ->

            shortenerKeyBlacklist.each {
                if (it == val) errors.rejectValue('shortenerKey', 'unique')
            }

        }
        destinationUrl url: true, nullable: false
        userCreated nullable: false
        validUntil nullable: true
    }

    static mapping = {
        shortenerKey index: 'shortenerKey_idx'
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
}
