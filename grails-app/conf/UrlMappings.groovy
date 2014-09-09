class UrlMappings {

	static mappings = {

        "/login/$action?"(controller: "login")
        "/logout/$action?"(controller: "logout")

        "/shorteners"(resources:"shortener")


        "/$shortenerKey" {
            controller = 'redirect'
            action = 'index'
            constraints {
                shortenerKey(matches:/[a-zA-Z0-9]*/)
            }
        }

        '/' (controller: 'shortener')
        //'/' redirect: 'http://www.manning.com', permanent: true
        '404'(view:'/notFound')
        '500'(view:'/error')


	}
}
