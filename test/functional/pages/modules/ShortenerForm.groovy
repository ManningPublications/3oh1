package pages.modules

import geb.Module

class ShortenerForm extends Module {
    static content = {

        _destinationUrl { $("#destinationUrl") }
        _validFrom_day { $("#validFrom_day") }
        _validFrom_month { $("#validFrom_month") }
        _validFrom_year { $("#validFrom_year") }

        _validUntil_day { $("#validUntil_day") }
        _validUntil_month { $("#validUntil_month") }
        _validUntil_year { $("#validUntil_year") }


        _saveButton { $("button.save") }

    }



    void setFormValues(String destinationUrl, Date validFrom = null, Date validUntil = null) {

        _destinationUrl = destinationUrl

        if (validFrom) {
            _validFrom_day = validFrom.format("d")
            _validFrom_month = validFrom.format("M")
            _validFrom_year = validFrom.format("yyyy")
        }

        if (validUntil) {
            _validUntil_day = validUntil.format("d")
            _validUntil_month = validUntil.format("M")
            _validUntil_year = validUntil.format("yyyy")
        }


    }

    void save() {
        _saveButton.click()
    }

}
