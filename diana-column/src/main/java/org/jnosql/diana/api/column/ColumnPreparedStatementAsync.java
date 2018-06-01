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
package org.jnosql.diana.api.column;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An object that represents a precompiled Query statement.
 */
public interface ColumnPreparedStatementAsync {


    /**
     * Binds an argument to a positional parameter.
     *
     * @param name     the parameter name
     * @param value    the parameter value
     * @return the same query instance
     * @throws NullPointerException     when there is null parameter
     * @throws IllegalArgumentException
     */
    ColumnPreparedStatementAsync bind(String name, Object value);

    /**
     * Executes a query and return the result as List
     *
     * @return The result list, if delete it will return an empty list
     */
    void getResultList(Consumer<List<ColumnEntity>> callBack);

    /**
     * Returns the result as a single element otherwise it will return an {@link Optional#empty()}
     *
     * @param callBack the result callback
     * @throws org.jnosql.diana.api.NonUniqueResultException when the result has more than one entity
     */
    void getSingleResult(Consumer<Optional<ColumnEntity>> callBack);

}