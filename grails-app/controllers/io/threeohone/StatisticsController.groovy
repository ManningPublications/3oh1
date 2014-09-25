package io.threeohone

import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class StatisticsController {

    def statisticsService


    def index() {

        def top5 = statisticsService.getTopShorteners()

        def totalNumbersPerMonth = [324, 552, 650, 550, 411, 290, 350, 850, 900, 950, 930, 600]

        respond RedirectLog.list(max: 5, fetch: [shortener: "eager"]), model: [
                top5: top5,
                totalNumbersPerMonth: totalNumbersPerMonth
        ]
    }
}
