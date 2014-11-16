databaseChangeLog = {

    changeSet(author: "mario (generated)", id: "1416120497231-1") {

        createTable(tableName: "client_information") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "client_informPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "browser_name", type: "varchar(255)")

            column(name: "browser_version", type: "varchar(255)")

            column(name: "mobile_browser", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "operating_system", type: "varchar(255)")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-2") {
        createTable(tableName: "client_location") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "client_locatiPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "area_code", type: "varchar(255)")

            column(name: "city", type: "varchar(255)")

            column(name: "country_code", type: "varchar(255)")

            column(name: "country_name", type: "varchar(255)")

            column(name: "dma_code", type: "varchar(255)")

            column(name: "latitude", type: "varchar(255)")

            column(name: "longitude", type: "varchar(255)")

            column(name: "metro_code", type: "varchar(255)")

            column(name: "postal_code", type: "varchar(255)")

            column(name: "region", type: "varchar(255)")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-3") {
        createTable(tableName: "redirect_log") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "redirect_logPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "client_information_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "client_location_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "referer", type: "varchar(255)")

            column(name: "shortener_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-4") {
        createTable(tableName: "role") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-5") {
        createTable(tableName: "shortener") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "shortenerPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "destination_url", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "key", type: "varchar(255)")

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "user_created_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "valid_from", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "valid_until", type: "timestamp")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-6") {
        createTable(tableName: "user") {
            column(autoIncrement: "true", name: "id", type: "bigint") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "userPK")
            }

            column(name: "version", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "enabled", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "password", type: "varchar(255)") {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: "boolean") {
                constraints(nullable: "false")
            }

            column(name: "username", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-7") {
        createTable(tableName: "user_role") {
            column(name: "role_id", type: "bigint") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-8") {
        addPrimaryKey(columnNames: "role_id, user_id", constraintName: "user_rolePK", tableName: "user_role")
    }

    changeSet(author: "mario (generated)", id: "1416120497231-15") {
        createIndex(indexName: "UK_irsamgnera6angm0prq1kemt2", tableName: "role", unique: "true") {
            column(name: "authority")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-16") {
        createIndex(indexName: "authority_uniq_1416120497159", tableName: "role", unique: "true") {
            column(name: "authority")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-17") {
        createIndex(indexName: "UK_qgn160t4t99bsnthnk8pn5lev", tableName: "shortener", unique: "true") {
            column(name: "key")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-18") {
        createIndex(indexName: "key_idx", tableName: "shortener") {
            column(name: "key")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-19") {
        createIndex(indexName: "key_uniq_1416120497167", tableName: "shortener", unique: "true") {
            column(name: "key")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-20") {
        createIndex(indexName: "UK_sb8bbouer5wak8vyiiy4pf2bx", tableName: "user", unique: "true") {
            column(name: "username")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-21") {
        createIndex(indexName: "username_uniq_1416120497174", tableName: "user", unique: "true") {
            column(name: "username")
        }
    }

    changeSet(author: "mario (generated)", id: "1416120497231-9") {
        addForeignKeyConstraint(baseColumnNames: "client_information_id", baseTableName: "redirect_log", constraintName: "FK_c1g6tca1ef825lr7y0qpdyvbh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "client_information", referencesUniqueColumn: "false")
    }

    changeSet(author: "mario (generated)", id: "1416120497231-10") {
        addForeignKeyConstraint(baseColumnNames: "client_location_id", baseTableName: "redirect_log", constraintName: "FK_1dgr83qi45y8xapcuwo95wei2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "client_location", referencesUniqueColumn: "false")
    }

    changeSet(author: "mario (generated)", id: "1416120497231-11") {
        addForeignKeyConstraint(baseColumnNames: "shortener_id", baseTableName: "redirect_log", constraintName: "FK_2ho59uq46cxopt5e2yp5g4tih", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "shortener", referencesUniqueColumn: "false")
    }

    changeSet(author: "mario (generated)", id: "1416120497231-12") {
        addForeignKeyConstraint(baseColumnNames: "user_created_id", baseTableName: "shortener", constraintName: "FK_cqtswrkwn6tq5ix3mrneq9jc6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
    }

    changeSet(author: "mario (generated)", id: "1416120497231-13") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FK_it77eq964jhfqtu54081ebtio", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
    }

    changeSet(author: "mario (generated)", id: "1416120497231-14") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FK_apcc8lxk2xnug8377fatvbn04", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
    }
}