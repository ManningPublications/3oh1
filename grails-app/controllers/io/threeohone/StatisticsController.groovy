package io.threeohone

import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class StatisticsController {

    def statisticsService


    def index() {

        def top5 = statisticsService.getTopShorteners()

        def totalNumbersPerMonth = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date())

        def numbers = totalNumbersPerMonth.collect { it.redirectCounter }

        respond RedirectLog.list(max: 5, fetch: [shortener: "eager"]), model: [
                top5: top5,
                totalNumbersPerMonth: numbers
        ]
    }
}
