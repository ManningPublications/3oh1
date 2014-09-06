package pages

import geb.Page

class ShortenerShowPage extends Page {

    static at = { title == "Show Shortener" }

    static content = {

        _flash(required: true) { $("div.alert.alert-warning") }
        _shortUrl { $("#shortUrl a") }
    }

    void clickShortUrl() {
        _shortUrl.click()
    }

    void isSuccessMessageHere() {
        _flash.displayed
    }

}