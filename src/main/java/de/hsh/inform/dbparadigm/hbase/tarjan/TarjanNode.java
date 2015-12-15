package de.hsh.inform.dbparadigm.hbase.tarjan;

import java.util.List;
import java.util.Vector;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;

public class TarjanNode implements INode {

	private String identifierString;
	private List<IEdge> in;
	private List<IEdge> out;
	private int index;
	private int link;
	private boolean visited;
	
	public TarjanNode (String identifierString) {
		this.identifierString = identifierString;
		this.in = new Vector<>();
		this.out = new Vector<>();
		this.index = -1;
		this.link = -2;
		this.setVisited(false);
	}
	  
	public void adjustLink (int value) {
		if (value < this.link) this.link = value;
	}
	  
	public boolean linkIsIndex () {
		return this.link == this.index;
	}

	@Override
	public String getIdentifierString() {
		return this.identifierString;
	}

	@Override
	public void setIdentifierString(String id) {
		this.identifierString = id;
	}

	@Override
	public List<IEdge> getIncomingEdges() {
		return this.in;
	}

	@Override
	public void setIncomingEdges(List<IEdge> edges) {
		this.in = edges;
	}

	@Override
	public List<IEdge> getOutgoingEdges() {
		return out;
	}

	@Override
	public void setOutgoingEdges(List<IEdge> edges) {
		this.out = edges;
	}
	
	public int getIndex() {
		return index;
	}

	public int getLink() {
		return link;
	}

	public void setIndex (int index) {
		this.index = index;
	}
	
	public void setLink (int link) {
		this.link = link;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public String toString () {
		return "TN(" + identifierString + ")";
	}
	
}