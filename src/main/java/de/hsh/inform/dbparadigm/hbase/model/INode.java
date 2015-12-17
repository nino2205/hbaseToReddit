package de.hsh.inform.dbparadigm.hbase.model;

import java.util.List;

public interface INode {
	public String getIdentifierString();
	public void setIdentifierString(String id);
	public List<IEdge> getIncomingEdges();
	public void setIncomingEdges(List<IEdge> edges);
	public void addIncomingEdges(IEdge edge);
	public List<IEdge> getOutgoingEdges();
	public void setOutgoingEdges(List<IEdge> edges);
	public void addOutgoingEdges(IEdge edge);
	public Long getLastActivity();
	public void setLastActivity(Long lastActivity);
	public List<INode> getNeighbours ();
}
