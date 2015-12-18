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

	public Node(String identifier, String name, Long lastActivity, List<IEdge> incomingEdges,
			List<IEdge> outgoingEdges) {
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
	public List<INode> getNeighbours() {
		ArrayList<INode> neighbors = new ArrayList<>();
		if (getOutgoingEdges() != null) {
			for (IEdge edge : getOutgoingEdges()) {
				neighbors.add(edge.getDestination());
			}
		}
		if (getIncomingEdges() != null) {
			for (IEdge edge : getIncomingEdges()) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	@Override
	public String toString() {
		return "Node{" + "name='" + name + '\'' + ", lastActivity=" + lastActivity + ", identifier='" + identifier
				+ '\'' + ", incoming='" + (incomingEdges != null ? incomingEdges.size() + "'" : "null'")
				+ ", outgoing='" + (outgoingEdges != null ? outgoingEdges.size() + "'" : "null'") + '}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((lastActivity == null) ? 0 : lastActivity.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

}
