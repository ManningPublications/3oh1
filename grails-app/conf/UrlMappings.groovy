class UrlMappings {

	static mappings = {

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/dbconsole"(controller: "db")

        "/shorteners"(resources:"shortener")
        "/api/shorteners"(resources:"shortener")



        "/$shortenerKey" {
            controller = 'redirect'
            action = 'index'
            constraints {
                shortenerKey(matches:/[a-zA-Z0-9]*/)
            }
        }

        '/' (controller: 'shortener')
        //'/' redirect: 'http://www.threeohone.com', permanent: true
        '404'(view:'/notFound')
        '500'(view:'/error')

        name apiDocs: "/docs/api"(view:"docs/api")



    }
}
