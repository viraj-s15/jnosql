/*
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
 */
package org.jnosql.artemis.graph;

import jakarta.nosql.NonUniqueResultException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The Graph Traversal that maps {@link org.apache.tinkerpop.gremlin.structure.Element}.
 * This Traversal is lazy, in other words, that just run after the
 */
public interface ValueMapTraversal {


    /**
     * Get all the result in the traversal as Stream
     *
     * @return the entity result as {@link Stream}
     */
    Stream<Map<String, Object>> stream();

    /**
     * Get the next n-number of results from the traversal.
     *
     * @param limit the limit to result
     * @return the entity result as {@link Stream}
     */
    Stream<Map<String, Object>> next(int limit);

    /**
     * Returns the next elements in the traversal.
     *
     * @return the map
     */
    Map<String, Object> next();

    /**
     * Concludes the traversal that returns a single result
     *
     * @return the entity result otherwise {@link Optional#empty()}
     * @throws NonUniqueResultException when there is more than one result
     */
    Optional<Map<String, Object>> getSingleResult();

    /**
     * Concludes the traversal then returns the result as list.
     *
     * @return the maps result
     */
    List<Map<String, Object>> getResultList();

    /**
     * Map the traversal next to its reduction as a sum of the elements
     *
     * @return the sum
     */
    long count();
}