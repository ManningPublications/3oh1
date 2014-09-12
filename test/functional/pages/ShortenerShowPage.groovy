package pages

import geb.Page

class ShortenerShowPage extends Page {

    static at = { title == "Show Shortener" }

    static content = {

        _flash(required: true) { $("div.alert.alert-warning") }
        _notActiveMessage(required: true) { $("#shortener-not-active-warning") }

        _shortUrl { $("#shortUrl a") }
        _editButton { $("#editShortener") }
    }

    void clickShortUrl() {
        _shortUrl.click()
    }

    boolean isSuccessMessageHere() {
        _flash.displayed
    }

    void editShortener() {
        _editButton.click()
    }

    boolean isShortenerActive() {
        !_notActiveMessage.displayed
    }

}