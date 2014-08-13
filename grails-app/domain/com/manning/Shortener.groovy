package com.manning

class Shortener {

    String destinationUrl
    String shortenedUrl

    Date validFrom
    Date validUntil

    String userCreated

    static constraints = {
        shortenedUrl unique: true
        validUntil nullable: true
    }
}
