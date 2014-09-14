package com.manning

import com.manning.security.User

class Shortener {

    enum Validity {
        ACTIVE, EXPIRED, FUTURE
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

    def static getCustomClosureByValidity(def validity) {

        def now = new Date()

        switch (validity) {
            case 'active': getValidityActiveClosure(now); break;
            case 'expired': getValidityExpiredClosure(now); break;
            case 'future': getValidityFutureClosure(now); break;
        }

    }

    def static getValidityActiveClosure(Date now) {
        return {
            le('validFrom', now)
            and {
                or {
                    isNull('validUntil')
                    ge('validUntil', now)
                }
            }

        }
    }

    def static getValidityExpiredClosure(Date now) {
        return { lt("validUntil", now) }
    }

    def static getValidityFutureClosure(Date now) {
        return { gt("validFrom", now) }
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
