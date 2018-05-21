package pages

import geb.Page
import org.openqa.selenium.Keys
import pages.modules.Navbar
import pages.modules.ShortenerTable

class ShortenerIndexPage extends Page {
    static url = "/3oh1/shorteners?lang=en"
    static at = { title == "Shortener List" }

    static content = {

        navbar { module Navbar }

        _addButton { $("#addShortener") }
        _searchBox { $("#search") }

        _shortenerTable { module ShortenerTable, $("#shortenerList") }
        _destinationColHead { $("a", text: "Destination") }
        _pagingLinkTwo { $("a", text: "2") }

        _flash(required: true) { $("div.alert.alert-warning") }

    }

    boolean isSuccessMessageHere() {
        _flash.displayed
    }


    int numberOfShorteners() {
        _shortenerTable.size
    }

    void showValidity(String validity) {
        $("#${validity}").click()
    }

    void addShortener() {
        _addButton.click()
    }

    void search(String query) {
        _searchBox = query
        _searchBox << Keys.ENTER
    }

    boolean containsShortener(String destinationUrl) {
        _shortenerTable.hasShortener(destinationUrl)
    }

    void sortDestinationURL() {
        _destinationColHead.click()
    }

    void pageOnPageTwo(){
        _pagingLinkTwo.click()
    }

}