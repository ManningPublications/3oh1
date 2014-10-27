package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured(['permitAll'])
class RedirectController {

        def shortenerService

        def redirectLoggingService

        def index() {

            def key = params.shortenerKey
            def shortener

            if (key) {
                shortener = shortenerService.findActiveShortenerByKey(key)
            }

            if (!shortener) {
                render status: HttpStatus.NOT_FOUND, view: '/notFound'
            }
            else {
                redirectLoggingService.log(
                        shortener: shortener,
                        referer: request.getHeader("referer"),
                        clientIp: request.remoteAddr
                )
                redirect url: shortener.destinationUrl, permanent: true
            }

        }
}
