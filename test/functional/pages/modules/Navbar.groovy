package pages.modules

import geb.Module

class Navbar extends Module {

    static content = {

        _shortenersMenu { $("#shortenersMenu") }
        _myShorteners(required: false) { $("#myShorteners") }
        _allShorteners(required: false) { $("#allShorteners") }
        _statistics { $("#statistics") }

        _logoutButton { $("#logout") }

    }

    void allShorteners() {
        _shortenersMenu.click()
        _allShorteners.click()
    }

    void myShorteners() {
        _shortenersMenu.click()
        _myShorteners.click()
    }

    void statistics() {
        _statistics.click()
    }

    void logout() {
        _logoutButton.click()
    }


}
