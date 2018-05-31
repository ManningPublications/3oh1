package io.threeohone.security

class Role {

	long id
	String authority

	static mapping = {
		datasource 'userLookup'
		id params: [sequence: 'role_id_seq']
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
