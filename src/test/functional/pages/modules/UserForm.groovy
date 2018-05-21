package pages.modules

import geb.Module

class UserForm extends Module {
    static content = {
        _username { $('#username') }
        _role { $('#role') }
        _saveButton { $("button.save") }

    }

    void setRoleByValue(String value) {
        _role.value(value)
    }

    void save() {
        _saveButton.click()
    }

}
