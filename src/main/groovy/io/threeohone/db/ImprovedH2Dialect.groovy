package io.threeohone.db

import org.hibernate.dialect.H2Dialect;

/**
 * Workaround for unnecessary error messages that occur in grails 2.4 + h2db
 *
 * @see https://hibernate.onjira.com/browse/HHH-7002
 * @see http://stackoverflow.com/questions/23858953/grails-2-4-and-hibernate4-errors-with-run-app
 *
 */
public class ImprovedH2Dialect extends H2Dialect {
    @Override
    public String getDropSequenceString(String sequenceName) {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just
        // leads to error messages about missing tables when we don't have a
        // schema in the database
        return false;
    }
}