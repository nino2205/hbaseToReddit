package de.hsh.inform.dbparadigm.hbase.model;

public interface IEdge {
	public String getIdentifierString();

	public void setIdentifierString(String id);

	public INode getSource();

	public void setSource(INode node);

	public INode getDestination();

	public void setDestination(INode node);

	public Long getCreated();

	public void setCreated(Long created);

	public void setTitle(String title);

	public String getTitle();
}
