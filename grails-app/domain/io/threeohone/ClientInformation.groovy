package io.threeohone

class ClientInformation {

    String browserName
    String browserVersion
    String operatingSystem

    boolean mobileBrowser


    static belongsTo = [redirectLog: RedirectLog]

    static constraints = {
        browserName nullable: true
        browserVersion nullable: true
        operatingSystem nullable: true
    }

}
