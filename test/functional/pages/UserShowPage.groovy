package pages

import geb.Page
import pages.modules.Navbar

class UserShowPage extends Page {

    static at = { title == "Show User" }

    static content = {

        _flash(required: true) { $("div.alert.alert-warning") }

        _changePassword { $("#changePassword") }
        _edit { $("#edit")}
        _delete {$('#delete')}

        navbar { module Navbar }
    }

    boolean isSuccessMessageHere() {
        _flash.displayed
    }

    void changePassword() {
        _changePassword.click()
    }

    void edit() {
        _edit.click()
    }

    void deleteUser(){
        _delete.click()
    }


}