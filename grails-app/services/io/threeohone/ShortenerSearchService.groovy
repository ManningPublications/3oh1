package io.threeohone

import org.hibernate.sql.JoinType

/**
 * Created by bresche on 23.09.14.
 */
class ShortenerSearchService {

    def tokens


    /**
     *
     * @param query query string for searching
     * @param validity the shorteners validity
     * @param max max result per page
     * @param offset
     * @param sort
     * @param order
     * @return the found resultlist as orm.PagedResultList
     */
    def search(String query, Shortener.Validity validity, Integer max, offset, sort, order) {

        if (query == null) query = ''
        tokens = query.split(' ')
        def now = new Date()

        Shortener.createCriteria().list([max: max, offset: offset, sort: sort, order: order]) {
            createAlias('userCreated', 'u', JoinType.LEFT_OUTER_JOIN)
            for (token in tokens) {
                and {
                    or {
                        ilike('destinationUrl', "%${token}%")
                        ilike('shortenerKey', "%${token}%")
                        ilike('u.username', "%${token}%")
                    }
                }
            }

            if (validity == Shortener.Validity.ACTIVE) {
                le('validFrom', now)
                and {
                    or {
                        isNull('validUntil')
                        ge('validUntil', now)
                    }
                }
            }

            if (validity == Shortener.Validity.EXPIRED) {
                lt("validUntil", now)
            }

            if (validity == Shortener.Validity.FUTURE) {
                gt("validFrom", now)
            }
        }
    }
}