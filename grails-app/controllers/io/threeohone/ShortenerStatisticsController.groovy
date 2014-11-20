package io.threeohone

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.annotation.Secured


@Secured(['isAuthenticated()'])
class ShortenerStatisticsController {


    StatisticsService statisticsService

    def show() {

        def shortenerInstance = Shortener.findByKey(params.shortenerId)

        if (!params.shortenerId || !shortenerInstance) {
            response.status = 404
            return
        }

        def totalNumberOfRedirectsPerMonth = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)

        def monthNames = totalNumberOfRedirectsPerMonth.collect { "${it.month} / ${it.year}" }

        def redirectCounters = totalNumberOfRedirectsPerMonth.collect { it.redirectCounter }

        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()

        def statisticsResponse = [
                key: shortenerInstance.key,
                redirectCounter: redirectCounter,
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                monthNames: monthNames,
                redirectCounters: redirectCounters
        ]

        render statisticsResponse as JSON
    }


}
