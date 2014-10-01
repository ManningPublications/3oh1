package io.threeohone

import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class StatisticsController {

    def statisticsService


    def index() {

        def top5 = statisticsService.getTopShorteners()
        def redirectCounterTotal = RedirectLog.count()

        def result = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date())

        def totalNumberOfRedirectsPerMonth = [monthNames: [], redirectCounters: []]
        totalNumberOfRedirectsPerMonth.monthNames = result.collect { "${it.month} / ${it.year}" }
        totalNumberOfRedirectsPerMonth.redirectCounters = result.collect { it.redirectCounter }

        respond RedirectLog.list(max: 15, fetch: [shortener: "eager"]), model: [
                top5: top5,
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                redirectCounterTotal: redirectCounterTotal
        ]
    }
}
