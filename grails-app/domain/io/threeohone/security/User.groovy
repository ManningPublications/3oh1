package io.threeohone.security

class User {

	transient springSecurityService

	long id
	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['springSecurityService']

	static mapping = {
		datasource 'userLookup'
		table 'users'
		id params: [sequence: 'users_id_seq']
		password column: '`password`'
	}

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	Set<Role> getAuthorities() {
		UserRole.withTransaction{UserRole.findAllByUser(this, [fetch:['role': 'join']]).collect { it.role }}
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
