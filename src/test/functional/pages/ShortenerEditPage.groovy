package pages

import geb.Page
import pages.modules.Navbar
import pages.modules.ShortenerForm

class ShortenerEditPage extends Page {

    static at = { title == "Edit Shortener" }

    static content = {

        form { module ShortenerForm }

        navbar { module Navbar }
    }

    void updateShortener(String destinationUrl, Date validFrom = null, Date validUntil = null) {
        form.setFormValues(destinationUrl, validFrom, validUntil)
        form.save()
    }

}