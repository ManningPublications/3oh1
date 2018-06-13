package io.threeohone

import grails.plugin.springsecurity.annotation.Secured

@Secured(['isAuthenticated()'])
class StatisticsController {

    def statisticsService


    def index() {
        def redirectLogList = RedirectLog.list(max:10, fetch: [shortener: "join"])
        def top5 = statisticsService.getTopShorteners()
        def redirectCounterTotal = RedirectLog.count()
        def redirectCountersPerOperatingSystem = statisticsService.getRedirectCounterGroupedBy(null, 'operatingSystem')
        def redirectCountersPerBrowser = statisticsService.getRedirectCounterGroupedBy(null, 'browserName')

        def result = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date())

        def totalNumberOfRedirectsPerMonth = [monthNames: [], redirectCounters: []]
        totalNumberOfRedirectsPerMonth.monthNames = result.collect { "${it.month} / ${it.year}" }
        totalNumberOfRedirectsPerMonth.redirectCounters = result.collect { it.redirectCounter }

        respond ([
                redirectLogInstanceList: redirectLogList,
                top5: top5,
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                redirectCounterTotal: redirectCounterTotal,
                redirectCountersPerOperatingSystem: redirectCountersPerOperatingSystem,
                redirectCountersPerBrowser: redirectCountersPerBrowser
        ])
    }

    def shortener() {
        Shortener shortenerInstance = Shortener.findByKey(params.id)

        def result = statisticsService.getTotalRedirectsPerMonthBetween(new Date() - 365, new Date(), shortenerInstance)
        def redirectCountersPerOperatingSystem = statisticsService.getRedirectCounterGroupedBy(shortenerInstance, 'operatingSystem')
        def redirectCountersPerBrowser = statisticsService.getRedirectCounterGroupedBy(shortenerInstance, 'browserName')

        def totalNumberOfRedirectsPerMonth = [monthNames: [], redirectCounters: []]
        totalNumberOfRedirectsPerMonth.monthNames = result.collect { "${it.month} / ${it.year}" }
        totalNumberOfRedirectsPerMonth.redirectCounters = result.collect { it.redirectCounter }

        def redirectCounter = RedirectLog.where { shortener == shortenerInstance }.count()

        respond ([
                totalNumberOfRedirectsPerMonth: totalNumberOfRedirectsPerMonth,
                redirectCounter: redirectCounter,
                redirectCountersPerOperatingSystem: redirectCountersPerOperatingSystem,
                redirectCountersPerBrowser: redirectCountersPerBrowser
        ])

    }
}
