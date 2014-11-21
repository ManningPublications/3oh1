package io.threeohone

import io.threeohone.Shortener.Validity
import io.threeohone.security.User
import org.hibernate.sql.JoinType

class ShortenerSearchService {


    /**
     * searches for shorteners by their attributes. It can be used by setting the query string.
     * Search tokes are seperated by a space. The result list contains shorteners that fulfill
     * all search tokens.
     *
     * E.g. there are two shorteners 'test.com from 'username' and 'test2.com' from 'username'
     * When a search for 'test.com username' is executed only the first shortener is returned.
     *
     * @param query query string for searching
     * @param validity the shorteners validity
     * @param paginationParams the params to be used for pagination (max, offset, sort, order)
     *
     * @return the found resultlist as orm.PagedResultList
     */
    def search(String query, Shortener.Validity validity, Map paginationParams) {
        searchByUser(query, validity, null, paginationParams)
    }


    /**
     * searches for shorteners by their attributes. It can be used by setting the query string.
     * Search tokes are seperated by a space. The result list contains shorteners that fulfill
     * all search tokens.
     *
     * E.g. there are two shorteners 'test.com from 'username' and 'test2.com' from 'username'
     * When a search for 'test.com username' is executed only the first shortener is returned.
     *
     * @param query query string for searching
     * @param validity the shorteners validity
     * @param paginationParams the params to be used for pagination (max, offset, sort, order)
     *
     * @return the found resultlist as orm.PagedResultList
     */
    def searchByUser(String query, Validity validity, User userCreated, Map paginationParams) {

        if (query == null) query = ''
        def tokens = query.split(' ')

        def now = new Date()

        Shortener.createCriteria().list(paginationParams) {
            createAlias('userCreated', 'u', JoinType.LEFT_OUTER_JOIN)
            for (token in tokens) {
                and {
                    or {
                        ilike('destinationUrl', "%${token}%")
                        ilike('key', "%${token}%")
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

            if (userCreated) {
                eq("userCreated", userCreated)
            }

        }
    }
}