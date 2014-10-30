package pages.modules

import geb.Module

class UserForm extends Module {
    static content = {
        _username { $('#username') }
        _password { $('#password') }
        _confirmPassword { $('#confirmPassword') }
        _saveButton { $("button.save") }

    }

    void setFormValues(String username, String password) {

        _username = username
        _password = password
        _confirmPassword = password
    }

    void setPassword(String password) {
        _password = password
        _confirmPassword = password
    }

    void save() {
        _saveButton.click()
    }

}
