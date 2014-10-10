package io.threeohone

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured


@Secured(['isAuthenticated()'])
class ShortenerStatisticsController {


    StatisticsService statisticsService

    def show() {

        def shortenerInstance = Shortener.get(params.shortenerId)

        if (!shortenerInstance) {
            response.status = 404
            return
        }

        def totalNumberOfRedirectsPerMonth = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)


        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()

        def statisticsResponse = [
                shortenerId: shortenerInstance.id,
                redirectCounter: redirectCounter,
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth
        ]

        render statisticsResponse as JSON
    }


}
