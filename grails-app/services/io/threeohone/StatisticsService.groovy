package io.threeohone


class StatisticsService {

    def getTopShorteners() {

        def c = RedirectLog.createCriteria()

        def queryResult = c.list {
            projections {
                countDistinct 'id', 'myCount'
                groupProperty 'shortener'
            }
            order('myCount', 'desc')
        }

        def result = queryResult.collect {
            [redirectCounter: it[0], shortener: it[1]]
        }

        return result
    }

    def getTotalRedirectsPerMonthBetween(Date start, Date end) {


        def c = RedirectLog.createCriteria()

        def queryResult = c.list {
            between("dateCreated", start, end)
            projections {
                countDistinct 'id', 'myCount'
                groupProperty('year')
                groupProperty('month')
            }
            order('myCount', 'desc')
        }

        def result = queryResult.collect {
            [redirectCounter: it[0], month: "${it[2]}/${it[1]}"]
        }

        return result
    }
}
