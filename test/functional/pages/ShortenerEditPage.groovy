package pages

import geb.Page

class ShortenerEditPage extends Page {
    static at = { title == "Edit Shortener" }

    static content = {

        _destinationUrl { $("#destinationUrl") }
        _saveButton { $("button.save") }
    }

    void updateShortener(String destinationUrl) {
        setFormValues(destinationUrl)
        save()
    }

    void setFormValues(String destinationUrl, Date validFrom = null, Date validUntil = null) {

        _destinationUrl = destinationUrl

    }

    void save() {
        _saveButton.click()
    }
}