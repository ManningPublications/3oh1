package pages

import geb.Page

class ShortenerCreatePage extends Page {
    static url = "/shorteners/create"
    static at = { title == "Create Shortener" }

    static content = {

        _destinationUrl { $("#destinationUrl") }
        _saveButton { $("button.save") }
    }

    void createShortener(String destinationUrl) {
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