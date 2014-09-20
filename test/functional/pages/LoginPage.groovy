package pages

import geb.Page

class LoginPage extends Page {
    static url = "/3oh1/login/auth?lang=en"
    static at = { title == "Login" }

    static content = {

        _username { $("#username") }
        _password { $("#password") }
        _loginButton { $("#submit") }
    }

    void login(String username, String password) {
        setFormValues(username, password)
        _loginButton.click()
    }

    void setFormValues(String username, String password) {

        _username = username
        _password = password

    }
}