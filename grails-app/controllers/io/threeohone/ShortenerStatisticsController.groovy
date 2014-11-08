package io.threeohone

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.annotation.Secured


@Secured(['isAuthenticated()'])
class ShortenerStatisticsController {


    StatisticsService statisticsService

    def show() {

        def shortenerInstance = Shortener.findByShortenerKey(params.shortenerId)

        if (!params.shortenerId || !shortenerInstance) {
            response.status = 404
            return
        }

        def totalNumberOfRedirectsPerMonth = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)


        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()

        def statisticsResponse = [
                shortenerKey: shortenerInstance.shortenerKey,
                redirectCounter: redirectCounter,
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth
        ]

        render statisticsResponse as JSON
    }


}
