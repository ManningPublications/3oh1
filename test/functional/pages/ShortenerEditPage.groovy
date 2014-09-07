package pages

import geb.Page

class ShortenerEditPage extends Page {
    static at = { title == "Edit Shortener" }

    static content = {

        _destinationUrl { $("#destinationUrl") }
        _userCreated { $("#userCreated") }
        _saveButton { $("button.save") }
    }

    void updateShortener(String destinationUrl, String userCreated) {
        setFormValues(destinationUrl,userCreated)
        save()
    }

    void setFormValues(String destinationUrl, String userCreated) {

        _destinationUrl = destinationUrl
        _userCreated = userCreated

    }

    void save() {
        _saveButton.click()
    }
}