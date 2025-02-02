/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.keyvalue;



import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.query.ArrayQueryValue;
import org.eclipse.jnosql.communication.query.Function;
import org.eclipse.jnosql.communication.query.FunctionQueryValue;
import org.eclipse.jnosql.communication.query.JSONQueryValue;
import org.eclipse.jnosql.communication.query.ParamQueryValue;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.eclipse.jnosql.communication.query.ValueType;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

final class Values {

    private Values() {
    }


    private static Object get(QueryValue<?> value, Params parameters) {

        ValueType type = value.type();
        switch (type) {
            case NUMBER:
            case STRING:
            case BOOLEAN:
                return value.get();
            case PARAMETER:
                return parameters.add(ParamQueryValue.class.cast(value).get());
            case ARRAY:
                return Stream.of(ArrayQueryValue.class.cast(value).get())
                        .map(v -> get(v, parameters))
                        .collect(toList());
            case FUNCTION:
                Function function = FunctionQueryValue.class.cast(value).get();
                String name = function.name();
                Object[] params = function.params();
                if ("convert".equals(name)) {
                    return executeConvert(parameters, params);
                }
                String message = String.format("There is not support to the function: %s with parameters %s", name,
                        Arrays.toString(params));
                throw new QueryException(message);
            case JSON:
                return JSONQueryValue.class.cast(value).get().toString();
            case CONDITION:
            default:
                throw new QueryException("There is not support to the value: " + type);

        }
    }

    private static Object executeConvert(Params parameters, Object[] params) {
        Object value = get(QueryValue.class.cast(params[0]), parameters);
        return Value.of(value)
                .get((Class<?>) params[1]);
    }

    static Value getValue(QueryValue<?> value, Params parameters) {
        Object result = get(value, parameters);
        if (result instanceof Value) {
            return Value.class.cast(result);
        }
        return Value.of(result);
    }
}