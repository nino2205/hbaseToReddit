package de.hsh.inform.dbparadigm.hbase.model;

import java.util.List;

/**
 * Created by Sören on 07.12.2015.
 */
public class Author implements INode {

    private String name;
    private Long lastActivity;

    public Author(String name, Long lastActivity) {
        this.name = name;
        this.lastActivity = lastActivity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Long lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Override
    public String getIdentifierString() {
        return null;
    }

    @Override
    public void setIdentifierString(String id) {

    }

    @Override
    public List<IEdge> getIncomingEdges() {
        return null;
    }

    @Override
    public void setIncomingEdges(List<IEdge> edges) {

    }

    @Override
    public List<IEdge> getOutgoingEdges() {
        return null;
    }

    @Override
    public void setOutgoingEdges(List<IEdge> edges) {

    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", lastActivity=" + lastActivity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (name != null ? !name.equals(author.name) : author.name != null) return false;
        return !(lastActivity != null ? !lastActivity.equals(author.lastActivity) : author.lastActivity != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (lastActivity != null ? lastActivity.hashCode() : 0);
        return result;
    }
}
