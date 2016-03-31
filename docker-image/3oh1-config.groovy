
def db = [
        host: System.getenv("DB_HOST"),
        name: System.getenv("DB_NAME"),
        user: System.getenv("DB_USER"),
        pass: System.getenv("DB_PASS"),
]

if (db.host && db.name && db.user && db.pass) {

    dataSource {
        dbCreate = ""
        url = "jdbc:postgresql://${db.host}:${db.port}/${db.name}"

        username = db.user
        password = db.pass

        properties {
            // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
            jmxEnabled = true
            initialSize = 5
            maxActive = 50
            minIdle = 5
            maxIdle = 25
            maxWait = 10000
            maxAge = 10 * 60000
            timeBetweenEvictionRunsMillis = 5000
            minEvictableIdleTimeMillis = 60000
            validationQuery = "SELECT 1"
            validationQueryTimeout = 3
            validationInterval = 15000
            testOnBorrow = true
            testWhileIdle = true
            testOnReturn = false
            jdbcInterceptors = "ConnectionState"
            defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
        }
    }
}