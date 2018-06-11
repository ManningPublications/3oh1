package io.threeohone

class RedirectLog {

    String referer
    ClientInformation clientInformation = new ClientInformation()
    ClientLocation clientLocation = new ClientLocation()


    Date dateCreated
    String month
    String year

    static belongsTo =  [shortener:Shortener]

    static constraints = {
        referer nullable: true
        month nullable: true
        year nullable: true
    }

    static mapping = {
        sort dateCreated: "desc"
        month formula: 'MONTH(DATE_CREATED)'
        year formula: 'YEAR(DATE_CREATED)'
        shortener cascade: 'all-delete-orphan'

        clientInformation fetch: 'join'
        clientLocation fetch: 'join'

    }
}
