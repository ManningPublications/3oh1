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


        if(end.before(start)) {
            return []
        }

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

        def monthList = getDefaultRedirectPerMonthList(start, end)


        queryResult.each { result ->
            def monthToAdd = monthList.find { month ->
                month.month == result[2] && month.year == result[1]
            }
            monthToAdd?.redirectCounter = result[0]
        }

        return monthList
    }


    def getDefaultRedirectPerMonthList(Date start, Date end) {
        clearDate(start)
        clearDate(end)

        def monthList = (start..end).findAll { it.format("d") == "1" }

        monthList.collect {
            [month: it.format("M"), year: it.format("yyyy"), redirectCounter: 0]
        }

    }

    private void clearDate(Date date) {
        date.clearTime()
        date.setDate(1)
    }
}
