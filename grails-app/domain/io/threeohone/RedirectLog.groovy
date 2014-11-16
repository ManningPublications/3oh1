package io.threeohone

class RedirectLog {

    Shortener shortener
    String referer
    ClientInformation clientInformation = new ClientInformation()
    ClientLocation clientLocation = new ClientLocation()


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

        clientInformation fetch: 'join'
        clientLocation fetch: 'join'

    }
}
