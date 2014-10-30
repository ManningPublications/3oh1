package pages

import geb.Page
import pages.modules.Navbar
import pages.modules.UserForm

class UserEditPage extends Page {

    static at = { title == "Edit User" }

    static content = {

        form { module UserForm }

        navbar { module Navbar }
    }

    void updateUsersPassword(String password) {
        form.setPassword(password)
        form.save()
    }

}