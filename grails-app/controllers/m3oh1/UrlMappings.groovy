package m3oh1

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/console"(controller: "console")

        "/dbconsole"(controller: "db")

        "/shorteners"(resources: "shortener") {
            "/redirectLog"(resource: "redirectLog", includes: ['show'])
        }

        "/api/shorteners"(resources: "shortener") {
            "/statistics"(resource: "shortenerStatistics", includes: ['show'])
            "/redirectLog"(resource: "redirectLog", includes: ['show'])
        }

        "/users"(resources: "user") {
            "/shorteners"(resources: "shortener", includes: ['index'])
            "/password"(resource: "password", includes: ['edit', 'update'])
        }

        "/api/users"(resources: "user") {
            "/shorteners"(resources: "shortener", includes: ['index'])
            "/password"(resource: "password", includes: ['update'])
        }

        "/$key" {
            controller = 'redirect'
            action = 'index'
            constraints {
                key(matches: /[a-zA-Z0-9]*/)
            }
        }

        '/'(controller: 'shortener')
        //'/' redirect: 'http://www.3oh1.io', permanent: true


        '404'(view: '/notFound')
        '500'(view: '/error')

        name apiDocs: "/docs/api"(view: "docs/api")
    }
}
