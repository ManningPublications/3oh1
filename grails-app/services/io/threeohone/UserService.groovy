package io.threeohone

import grails.gorm.transactions.Transactional
import io.threeohone.security.User

@Transactional
class UserService {

    User get(Long id) {
        User.withTransaction{User.findByUsername(id)}
    }
}
