package com.manning


class RedirectFinderService {

    static transactional = false

    public String findRedirectionUrlForKey(String shortenerKey) {
        def shortener = Shortener.findByShortenerKey(shortenerKey)

        def destinationUrl = null

        if (isInValidRedirectionDate(shortener)) {
            destinationUrl =  shortener.destinationUrl
        }

        return destinationUrl
    }

    boolean isInValidRedirectionDate(Shortener shortener) {
        def now = new Date()

        return shortener && shortener.validFrom < now && now < shortener.validUntil
    }

}
