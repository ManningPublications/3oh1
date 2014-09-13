package pages

import geb.Page

class ShortenerIndexPage extends Page {
    static url = "/redirector/shorteners?lang=en"
    static at = { title == "Shortener List" }

    static content = {

        _addButton { $("#addShortener") }
        _logoutButton { $("#logout") }
    }

    void addShortener() {
        _addButton.click()
    }

    void logout() {
        _logoutButton.click()
    }
}