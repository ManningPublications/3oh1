class UrlMappings {

	static mappings = {

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/dbconsole"(controller: "db")

        "/shorteners"(resources:"shortener")

        "/api/shorteners"(resources:"shortener") {
            "/statistics"(resource: "shortenerStatistics", includes: ['show'])
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

        '404'(view:'/notFound')
        '500'(view:'/error')

        name apiDocs: "/docs/api"(view:"docs/api")



    }
}
