package pages.modules

import geb.Module

class RoleForm extends Module {
    static content = {
        _username { $('#username') }
        _role { $('#role') }
        _saveButton { $("button.save") }

    }

    void setFormValues(String username, String password) {

        _username = username
        _password = password
        _confirmPassword = password
    }

    void setRoleByValue(String value) {

        _role.value(value)
    }

    void save() {
        _saveButton.click()
    }

}
