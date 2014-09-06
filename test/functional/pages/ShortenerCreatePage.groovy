package pages

import geb.Page

class ShortenerCreatePage extends Page {
    static url = "/shorteners/create"
    static at = { title == "Create Shortener" }

    static content = {

        _destinationUrl { $("#destinationUrl") }
        _userCreated { $("#userCreated") }
        _saveButton { $("button.save") }
    }

    void setFormValues(String destinationUrl, String userCreated) {

        _destinationUrl = destinationUrl
        _userCreated = userCreated

    }
    void save() {
        _saveButton.click()
    }
}