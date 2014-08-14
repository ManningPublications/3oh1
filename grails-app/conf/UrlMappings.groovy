class UrlMappings {

	static mappings = {
        "/$shortenerKey" {
            controller = 'redirect'
            action = 'index'
            constraints {
                shortenerKey(matches:/[a-zA-Z0-9]*/)
            }
        }
        '/' redirect: 'http://www.manning.com', permanent: true
        '404'(view:'/notFound')
        '500'(view:'/error')
	}
}
