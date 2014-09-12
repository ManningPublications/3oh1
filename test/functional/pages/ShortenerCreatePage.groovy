package pages

import geb.Page
import pages.modules.ShortenerForm

class ShortenerCreatePage extends Page {
    static url = "/shorteners/create"
    static at = { title == "Create Shortener" }

    static content = {
        form { module ShortenerForm }
    }

    void createShortener(String destinationUrl) {
        form.setFormValues(destinationUrl)
        form.save()
    }

}