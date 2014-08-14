class UrlMappings {

	static mappings = {

        "/"(controller: 'Redirect')
        "404"(view:'/error')
        "500"(view:'/error')
	}
}
