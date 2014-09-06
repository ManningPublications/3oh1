package com.manning


class RedirectFinderService {

    static transactional = false

    public String findRedirectionUrlForKey(String shortenerKey) {
        def shortener = Shortener.findByShortenerKey(shortenerKey)

        def destinationUrl = null

        if (shortener?.isActive()) {
            destinationUrl = shortener.destinationUrl
        }

        return destinationUrl
    }

}
