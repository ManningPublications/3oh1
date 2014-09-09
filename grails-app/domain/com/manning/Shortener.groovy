package com.manning

import com.manning.security.User

class Shortener {

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


    def static withValidityState(String validity = 'active', def params) {

        def now = new Date()

        switch(validity) {
            case 'active' : Shortener.where { validFrom <= now && validUntil >= now}; break;
            case 'expired' : Shortener.where { validUntil < now }; break;
            case 'future' : Shortener.where { validFrom > now }; break;
        }
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
