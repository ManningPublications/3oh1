package pages.modules

import geb.Module

import java.text.SimpleDateFormat

class ShortenerTable extends Module  {
        static content = {
            row { i -> module ShortenerRow, $("tbody tr", i) }
            size { $("tbody tr").size() }
    }

    boolean hasShortener(String destinationUrl) {

        boolean hasShortener = false

        for (int i = 0; i < size; i++) {
            if (row(i).destinationUrl.contains(destinationUrl)) {
                hasShortener = true
            }
        }

        return hasShortener

    }
}

class ShortenerRow extends Module {
    static content = {
        cell { i -> $("td", i) }

        key { cell(0).text() }
        destinationUrl { cell(1).text() }
        username { cell(2).text() }
        validFrom { new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(cell(4).text()) }
        validUntil { new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(cell(5).text()) }
    }
}