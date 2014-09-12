package com.manning.redirector

import grails.plugin.springsecurity.annotation.Secured


@Secured(['isAuthenticated()'])
class StatisticsController {


    def index() {
        respond RedirectLog.list(max: 5, fetch: [shortener: "eager"])
    }
}
