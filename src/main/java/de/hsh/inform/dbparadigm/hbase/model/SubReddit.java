package de.hsh.inform.dbparadigm.hbase.model;

import java.util.List;

/**
 * Created by Sören on 08.12.2015.
 */
public class SubReddit extends Node {
    public SubReddit(String id, String name, Long lastActivity) {
        super(id, name, lastActivity);
    }

    public SubReddit(String id, String name, Long lastActivity, List<IEdge> incomingEdges, List<IEdge> outgoingEdges) {
        super(id, name, lastActivity, incomingEdges, outgoingEdges);
    }
}
