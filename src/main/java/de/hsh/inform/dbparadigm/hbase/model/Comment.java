package de.hsh.inform.dbparadigm.hbase.model;

/**
 * Created by Sören on 07.12.2015.
 */
public class Comment implements IEdge {

    private String title;
    private Long created;

    public Comment(String title, Long created) {
        this.title = title;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String getIdentifierString() {
        return null;
    }

    @Override
    public void setIdentifierString(String id) {

    }

    @Override
    public INode getSource() {
        return null;
    }

    @Override
    public void setSource(INode node) {

    }

    @Override
    public INode getDestination() {
        return null;
    }

    @Override
    public void setDestination(INode node) {

    }

    @Override
    public String toString() {
        return "Comment{" +
                "title='" + title + '\'' +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (title != null ? !title.equals(comment.title) : comment.title != null) return false;
        return !(created != null ? !created.equals(comment.created) : comment.created != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }
}
