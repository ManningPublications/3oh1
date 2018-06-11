package io.threeohone

import grails.gorm.transactions.Transactional

@Transactional(readOnly = true)
class RedirectLogService {

    long countRedirects(Shortener shortener) {
        RedirectLog.where{shortener == shortener}.count()
    }
}
