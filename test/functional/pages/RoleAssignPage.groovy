package pages

import geb.Page
import pages.modules.Navbar
import pages.modules.PasswordForm
import pages.modules.RoleForm

class RoleAssignPage extends Page {

    static at = { title == "Assign role" }

    static content = {

        form { module RoleForm }

        navbar { module Navbar }
    }

    void assignRoleByValue(String value) {
        form.setRoleByValue(value)
        form.save()
    }

}