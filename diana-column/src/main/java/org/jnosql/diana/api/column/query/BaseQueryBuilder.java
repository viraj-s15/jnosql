/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

abstract class BaseQueryBuilder {

    protected String name;

    protected boolean negate;

    protected boolean and;

    protected ColumnCondition condition;

    protected <T> void eqImpl(T value) {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.eq(Column.of(name, value));
        appendCondition(newCondition);
    }

    protected void gtImpl(Number value) {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.gt(Column.of(name, value));
        appendCondition(newCondition);
    }

    protected void likeImpl(String value) {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.like(Column.of(name, value));
        appendCondition(newCondition);
    }

    protected void ltImpl(Number value) {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.lt(Column.of(name, value));
        appendCondition(newCondition);
    }

    protected void lteImpl(Number value) {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.lte(Column.of(name, value));
        appendCondition(newCondition);
    }
    protected void gteImpl(Number value) {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.gte(Column.of(name, value));
        appendCondition(newCondition);
    }

    protected void betweenImpl(Number valueA, Number valueB) {
        requireNonNull(valueA, "valueA is required");
        requireNonNull(valueB, "valueB is required");
        ColumnCondition newCondition = ColumnCondition.between(Column.of(name, asList(valueA, valueB)));
        appendCondition(newCondition);
    }

    protected <T> void inImpl(Iterable<T> values) {
        requireNonNull(values, "values is required");
        ColumnCondition newCondition = ColumnCondition.in(Column.of(name, values));
        appendCondition(newCondition);
    }


    protected void appendCondition(ColumnCondition newCondition) {
        if (negate) {
            newCondition = newCondition.negate();
        }
        if (nonNull(condition)) {
            if (and) {
                this.condition = condition.and(newCondition);
            } else {
                this.condition = condition.or(newCondition);
            }
        } else {
            this.condition = newCondition;
        }
        this.negate = false;
        this.name = null;
    }

}
