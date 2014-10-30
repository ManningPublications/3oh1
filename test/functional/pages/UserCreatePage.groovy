package pages

import geb.Page
import pages.modules.Navbar
import pages.modules.UserForm

class UserCreatePage extends Page {

    static at = { title == "Create User" }

    static content = {
        form { module UserForm }

        navbar { module Navbar }
    }

    void createUser(String username, String password) {
        form.setFormValues(username, password)
        form.save()
    }

}