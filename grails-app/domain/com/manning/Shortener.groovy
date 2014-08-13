package com.manning

class Shortener {

    String destinationUrl
    String shortenerKey

    Date validFrom
    Date validUntil

    String userCreated

    static constraints = {
        shortenerKey unique: true
        validUntil nullable: true
    }

    static mapping = {
        shortenerKey index: 'shortenerKey_idx'
    }
}
