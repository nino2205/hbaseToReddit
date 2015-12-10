package de.hsh.inform.dbparadigm.hbase.model;

import java.security.Principal;
import java.security.PublicKey;

/**
 * Created by Sören on 10.12.2015.
 */
public class Edge implements IEdge {
    private String identifier;
    private String title;
    private INode source;
    private INode destination;
    private Long created;

    public Edge(String identifier, String title, Long created, INode source, INode destination) {
        setTitle(title);
        setIdentifierString(identifier);
        setSource(source);
        setDestination(destination);
        setCreated(created);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getIdentifierString() {
        return identifier;
    }

    @Override
    public void setIdentifierString(String id) {
        identifier = id;
    }

    @Override
    public INode getSource() {
        return source;
    }

    @Override
    public void setSource(INode node) {
        source = node;
    }

    @Override
    public INode getDestination() {
        return destination;
    }

    @Override
    public void setDestination(INode node) {
        destination = node;
    }

    @Override
    public Long getCreated() {
        return created;
    }

    @Override
    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "identifier='" + identifier + '\'' +
                ", title='" + title + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (identifier != null ? !identifier.equals(edge.identifier) : edge.identifier != null) return false;
        if (title != null ? !title.equals(edge.title) : edge.title != null) return false;
        if (source != null ? !source.equals(edge.source) : edge.source != null) return false;
        if (destination != null ? !destination.equals(edge.destination) : edge.destination != null) return false;
        return !(created != null ? !created.equals(edge.created) : edge.created != null);

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }
}
