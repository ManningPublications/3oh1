package pages

import geb.Page
import pages.modules.Navbar
import pages.modules.PasswordForm

class PasswordEditPage extends Page {

    static at = { title == "Assign role" }

    static content = {

        form { module PasswordForm }

        navbar { module Navbar }
    }

    void updateUsersPassword(String password) {
        form.setPassword(password)
        form.save()
    }

}