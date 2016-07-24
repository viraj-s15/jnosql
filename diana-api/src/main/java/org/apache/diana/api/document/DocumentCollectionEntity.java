package org.apache.diana.api.document;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

public interface DocumentCollectionEntity extends Serializable {

    public static DocumentCollectionEntity of(String name) throws NullPointerException {
        return new DefaultDocumentCollectionEntity(name);
    }

    String getName();

    boolean remove(String columnName);

    List<Document> getDocuments();

    void add(Document document);

}
