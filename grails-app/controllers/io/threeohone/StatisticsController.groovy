package io.threeohone

import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class StatisticsController {


    def index() {

        def top5 = [
                [shortener: Shortener.first(), counter: 100],
                [shortener: Shortener.last(), counter: 95],
                [shortener: Shortener.first(), counter: 74],
                [shortener: Shortener.last(), counter: 31],
                [shortener: Shortener.first(), counter: 25],
        ]

        def totalNumbersPerMonth = [324, 552, 650, 550, 411, 290, 350, 850, 900, 950, 930, 600]

        respond RedirectLog.list(max: 5, fetch: [shortener: "eager"]), model: [
                top5: top5,
                totalNumbersPerMonth: totalNumbersPerMonth
        ]
    }
}
