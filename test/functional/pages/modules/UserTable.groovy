package pages.modules

import geb.Module

class UserTable extends Module  {
        static content = {
            row { i -> module UserRow, $("tbody tr", i) }
            size { $("tbody tr").size() }
    }

    boolean hasUser(String username) {

        boolean hasUser = false

        for (int i = 0; i < size; i++) {
            if (row(i).username.contains(username)) {
                hasUser = true
            }
        }

        return hasUser

    }
}

