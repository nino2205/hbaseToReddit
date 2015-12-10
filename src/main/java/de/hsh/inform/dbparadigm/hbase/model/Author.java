package de.hsh.inform.dbparadigm.hbase.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sören on 07.12.2015.
 */
public class Author extends Node {
    public Author(String id, String name, Long lastActivity) {
        super(id, name, lastActivity);
    }

    public Author(String id, String name, Long lastActivity, List<IEdge> incomingEdges, List<IEdge> outgoingEdges) {
        super(id, name, lastActivity, incomingEdges, outgoingEdges);
    }
}
