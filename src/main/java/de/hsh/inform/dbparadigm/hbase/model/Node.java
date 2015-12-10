package de.hsh.inform.dbparadigm.hbase.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sören on 10.12.2015.
 */
public class Node implements INode {
    private String name;
    private Long lastActivity;
    private String identifier;

    private List<IEdge> incomingEdges = new ArrayList<>();
    private List<IEdge> outgoingEdges = new ArrayList<>();

    public Node(String identifier, String name, Long lastActivity) {
        this.identifier = identifier;
        this.name = name;
        this.lastActivity = lastActivity;
    }

    public Node(String identifier, String name, Long lastActivity, List<IEdge> incomingEdges, List<IEdge> outgoingEdges) {
        this(identifier, name, lastActivity);
        this.incomingEdges.addAll(incomingEdges);
        this.outgoingEdges.addAll(outgoingEdges);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getLastActivity() {
        return lastActivity;
    }

    @Override
    public void setLastActivity(Long lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Override
    public String getIdentifierString() {
        return identifier;
    }

    @Override
    public void setIdentifierString(String id) {
        this.identifier = id;
    }

    @Override
    public List<IEdge> getIncomingEdges() {
        return null;
    }

    @Override
    public void addIncomingEdges(IEdge edge) {
        incomingEdges.add(edge);
    }

    @Override
    public void addOutgoingEdges(IEdge edge) {
        outgoingEdges.add(edge);
    }

    @Override
    public void setIncomingEdges(List<IEdge> edges) {
        incomingEdges.clear();
        incomingEdges.addAll(edges);
    }

    @Override
    public List<IEdge> getOutgoingEdges() {
        return outgoingEdges;
    }

    @Override
    public void setOutgoingEdges(List<IEdge> edges) {
        outgoingEdges.clear();
        outgoingEdges.addAll(edges);
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", lastActivity=" + lastActivity +
                ", identifier='" + identifier + '\'' +
                ", incoming='" + (incomingEdges != null ? incomingEdges.size() + "'" : "null'") +
                ", outgoing='" + (outgoingEdges != null ? outgoingEdges.size() + "'" : "null'") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (lastActivity != null ? !lastActivity.equals(node.lastActivity) : node.lastActivity != null) return false;
        if (identifier != null ? !identifier.equals(node.identifier) : node.identifier != null) return false;
        if (incomingEdges != null ? !incomingEdges.equals(node.incomingEdges) : node.incomingEdges != null)
            return false;
        return !(outgoingEdges != null ? !outgoingEdges.equals(node.outgoingEdges) : node.outgoingEdges != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (lastActivity != null ? lastActivity.hashCode() : 0);
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (incomingEdges != null ? incomingEdges.hashCode() : 0);
        result = 31 * result + (outgoingEdges != null ? outgoingEdges.hashCode() : 0);
        return result;
    }
}
