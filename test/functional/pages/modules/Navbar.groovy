package pages.modules

import geb.Module

class Navbar extends Module {

    static content = {

        _shorteners { $("#shorteners") }
        _statistics { $("#statistics") }

        _logoutButton { $("#logout") }

    }

    void shorteners() {
        _shorteners.click()
    }

    void statistics() {
        _statistics.click()
    }

    void logout() {
        _logoutButton.click()
    }


}
