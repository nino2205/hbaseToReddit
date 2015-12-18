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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

  
}
