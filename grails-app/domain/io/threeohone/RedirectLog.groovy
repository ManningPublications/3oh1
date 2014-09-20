package io.threeohone

import io.threeohone.Shortener

class RedirectLog {

    Shortener shortener
    String clientIp
    String referer
    String userAgent


    Date dateCreated

    static constraints = {
        referer nullable: true
    }

    static mapping = {
        sort dateCreated: "desc"
    }
}
