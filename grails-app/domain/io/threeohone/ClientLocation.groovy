package io.threeohone

class ClientLocation {

    String countryCode
    String countryName
    String region
    String city
    String postalCode
    String latitude
    String longitude
    String dmaCode
    String areaCode
    String metroCode

    static constraints = {

        countryCode nullable: true
        countryName nullable: true
        region nullable: true
        city nullable: true
        postalCode nullable: true
        latitude nullable: true
        longitude nullable: true
        dmaCode nullable: true
        areaCode nullable: true
        metroCode  nullable: true

    }
}
