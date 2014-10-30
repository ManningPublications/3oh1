class UrlMappings {

	static mappings = {

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/dbconsole"(controller: "db")

        "/shorteners"(resources:"shortener") {
            "/redirectLog"(resource: "redirectLog", includes: ['show'])
        }

        "/api/shorteners"(resources:"shortener") {
            "/statistics"(resource: "shortenerStatistics", includes: ['show'])
            "/redirectLog"(resource: "redirectLog", includes: ['show'])
        }



        "/$shortenerKey" {
            controller = 'redirect'
            action = 'index'
            constraints {
                shortenerKey(matches:/[a-zA-Z0-9]*/)
            }
        }

        '/' (controller: 'shortener')
        //'/' redirect: 'http://www.3oh1.io', permanent: true

        "/users"(resources:"user")
        "/api/users"(resources:"user")

        '404'(view:'/notFound')
        '500'(view:'/error')

        name apiDocs: "/docs/api"(view:"docs/api")



    }
}
