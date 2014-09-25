package io.threeohone


class StatisticsService {

    def getTopShorteners() {

        def c = RedirectLog.createCriteria()

        def queryResult = c.list {
            projections {
                countDistinct 'id', 'myCount'
                groupProperty 'shortener'
            }
            order ('myCount', 'desc')
        }

        def result = queryResult.collect {
            [redirectCounter: it[0], shortener: it[1]]
        }

        return result
    }

}
