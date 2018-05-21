package pages.modules

import geb.Module

class UserRow extends Module {
    static content = {
        cell { i -> $("td", i) }
        username { cell(0).text() }
    }
}