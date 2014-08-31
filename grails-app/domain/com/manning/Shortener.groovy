package com.manning

class Shortener {

    String destinationUrl
    String shortenerKey

    Date validFrom
    Date validUntil

    String userCreated

    static constraints = {
        shortenerKey unique: true, nullable: false
        destinationUrl url: true, nullable: false
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
}
