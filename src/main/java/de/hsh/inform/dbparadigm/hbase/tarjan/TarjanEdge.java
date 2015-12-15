package de.hsh.inform.dbparadigm.hbase.tarjan;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;

public class TarjanEdge implements IEdge {

	private String id;
	private INode source;
	private INode destination;
	
	public TarjanEdge (String id, INode source, INode destination) {
		this.id = id;
		this.source = source;
		this.destination = destination;
	}
	
	@Override
	public String getIdentifierString() {
		return id;
	}

	@Override
	public void setIdentifierString(String id) {
		this.id = id;
	}

	@Override
	public INode getSource() {
		return this.source;
	}

	@Override
	public void setSource(INode node) {
		this.source = node;
	}

	@Override
	public INode getDestination() {
		return this.destination;
	}

	@Override
	public void setDestination(INode node) {
		this.destination = node;
	}

}
