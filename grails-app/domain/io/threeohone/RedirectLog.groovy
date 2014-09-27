package io.threeohone

import io.threeohone.Shortener

class RedirectLog {

    Shortener shortener
    String clientIp
    String referer
    String userAgent


    Date dateCreated
    String month
    String year

    static constraints = {
        referer nullable: true
    }

    static mapping = {
        sort dateCreated: "desc"
        month formula: 'MONTH(DATE_CREATED)'
        year formula: 'YEAR(DATE_CREATED)'

    }
}
