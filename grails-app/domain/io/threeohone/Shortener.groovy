package io.threeohone

import io.threeohone.security.User

class Shortener {

    enum Validity {
        ACTIVE, EXPIRED, FUTURE
    }

    static Shortener.Validity getValidityByString(String validity) {
        if ((validity.toLowerCase()).equals('active')) return Shortener.Validity.ACTIVE
        if ((validity.toLowerCase()).equals('expired')) return Shortener.Validity.EXPIRED
        if ((validity.toLowerCase()).equals('future')) return Shortener.Validity.FUTURE
        return null
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
          after creation for generating the shortenerKey from the id, which is then persisted
         */
        shortenerKey unique: true, nullable: true
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
