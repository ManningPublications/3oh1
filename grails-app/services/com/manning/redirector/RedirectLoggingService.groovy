package com.manning.redirector

class RedirectLoggingService {

    def log(Map params) {

        new RedirectLog(
                shortener: params.shortener,
                clientIp: params.clientIp,
                referer: params.referer,
                userAgent: params.userAgent
        ).save(failOnError: true)
    }
}
