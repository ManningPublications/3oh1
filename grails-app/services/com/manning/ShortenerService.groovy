package com.manning


class ShortenerService {

    static transactional = true

    def hashidsService

    def springSecurityService

    def createShortener(def params) {

        def shortener = new Shortener(params)

        shortener.userCreated = springSecurityService.currentUser

        if (shortener.save(flush: true)) {

            createShortenerKey(shortener)

            shortener.save(flush: true)
        }

        return shortener

    }

    public Shortener findActiveShortenerByKey(String shortenerKey) {
        def shortener = Shortener.findByShortenerKey(shortenerKey)

        if (shortener?.isActive()) {
            return shortener
        }

        return null
    }


    private void createShortenerKey(Shortener shortener) {
        shortener.shortenerKey = hashidsService.encrypt(shortener.id)
    }
}
