package io.threeohone

class RedirectLog {

    Shortener shortener
    String clientIp
    String referer
    ClientInformation clientInformation


    Date dateCreated
    String month
    String year

    static constraints = {
        referer nullable: true
        month nullable: true
        year nullable: true
    }

    static mapping = {
        sort dateCreated: "desc"
        month formula: 'MONTH(DATE_CREATED)'
        year formula: 'YEAR(DATE_CREATED)'

    }
}
