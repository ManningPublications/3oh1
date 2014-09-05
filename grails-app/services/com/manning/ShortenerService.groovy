package com.manning


class ShortenerService {

    static transactional = true

    def hashidsService

    def createShortener(def params) {

        def shortener = new Shortener(params)

        if (shortener.save(flush: true)) {

            createShortenerKey(shortener)

            shortener.save(flush: true)
        }

        return shortener

    }

    private void createShortenerKey(Shortener shortener) {
        shortener.shortenerKey = hashidsService.encrypt(shortener.id)
    }
}
