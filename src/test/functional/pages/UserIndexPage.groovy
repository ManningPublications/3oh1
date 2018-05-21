package pages

import geb.Page
import pages.modules.Navbar
import pages.modules.UserTable

class UserIndexPage extends Page {
    static url = "/3oh1/users?lang=en"
    static at = { title == "User List" }

    static content = {
        _flash(required: true) { $("div.alert.alert-warning") }

        navbar { module Navbar }

        _addButton { $("#addUser") }

        _userTable { module UserTable, $("#userList") }

    }

    boolean isSuccessMessageHere() {
        _flash.displayed
    }

    int numberOfShorteners() {
        _userTable.size
    }

    void addUser() {
        _addButton.click()
    }

    boolean containsUser(String username) {
        _userTable.hasUser(username)
    }

    void showUser(String username) {
        $("a", text: username).click()
    }

}