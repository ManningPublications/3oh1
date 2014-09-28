package pages

import geb.Module
import geb.Page
import pages.modules.Navbar

import java.text.SimpleDateFormat

class StatisticsPage extends Page {

    static at = { title == "Statistics" }

    static content = {

        navbar { module Navbar }

        _lastRedirectsTable { module LastRedirectsTable, $("#lastRedirects") }

    }

    boolean isLastRedirect(String destinationUrl) {
        _lastRedirectsTable.isLastRedirect(destinationUrl)
    }
}


class LastRedirectsTable extends Module  {
    static content = {
        row { i -> module LastRedirectsRow, $("tbody tr", i) }
        size { $("tbody tr").size() }
    }

    boolean isLastRedirect(String destinationUrl) {
        row(0).destinationUrl.contains(destinationUrl)
    }
}

class LastRedirectsRow extends Module {
    static content = {
        cell { i -> $("td", i) }

        destinationUrl { cell(0).text() }
        time { new SimpleDateFormat("hh:mm").parse(cell(1).text()) }
    }
}