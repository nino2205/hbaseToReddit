package de.hsh.inform.dbparadigm.hbase.tarjan;

import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;

public class Algorithm {

	// The nodes to traverse.
	private final List<TarjanNode> nodes;
	
	// Ordered Set of visited nodes.
	private final Stack<TarjanNode> stack;
	
	// The strongly connected components (SCC).
	private Collection<Collection<TarjanNode>> components;
	
	// Used to assign unique incremental indicies. 
	private int pointer;
	
	public Algorithm (List<TarjanNode> nodes) {
		this.nodes = nodes;
		this.stack = new Stack<>();
		this.components = new Vector<>();
		this.pointer = 0;
	}
	
	/**
	 * Traverses all unvisited TajanNodes and returns the strong 
	 * components of this.nodes as a Collection of TarjanNode Collections.
	 */
	public Collection<Collection<TarjanNode>> execute () {
		int size = this.nodes.size();
		this.pointer = 0;
		this.components = new Vector<>();
		
		for (int i = 0; i < size; i++) {
			TarjanNode node = this.nodes.get(i);
			if (!node.isVisited()) this.visit(node);
		}
		return components;
	}
	
	/**
	 * Visits the TarjanNode node and it's unvisited neighbors. If the
	 * node's link equals it's index, a SCC is found.
	 */
	private void visit (TarjanNode node) {
		node.setLink(pointer);
		node.setIndex(pointer);
	    node.setVisited(true);
		this.stack.push(node);
	    this.pointer += 1;
	    
	    for (TarjanNode neighbour : this.getNeighbours(node)) {
	    	if (nodes.contains(neighbour) && !neighbour.isVisited()) {
	    		this.visit(neighbour);
	    		node.adjustLink(neighbour.getLink());
	    	} else if (stack.contains(neighbour)) {
	    		node.adjustLink(neighbour.getIndex());
	    	}
	    }
	    if (node.linkIsIndex()) {
	    	this.components.add(this.createComponent(node));
	    }
	}
	
	
	/**
	 * Creates a Component (Vector) from all TarjanNodes on the stack 
	 * until the TarjanNode node is reached (inclusively).
	 */
	private Vector<TarjanNode> createComponent (TarjanNode node) {
		Vector<TarjanNode> component = new Vector<TarjanNode>();
		
		while (true) {
			TarjanNode current = stack.pop();
			component.add(current);
			if (node.equals(current)) break;
		}
		return component;
	}
	
	/**
	 * Returns all neighbors of the INode in the tNode TarjanNode.
	 * TODO: Remove casting in the future.
	 */
	private Vector<TarjanNode> getNeighbours (TarjanNode node) {
		Vector<TarjanNode> neighbors = new Vector<>();
		for (IEdge edge : node.getOutgoingEdges()) {
			neighbors.add((TarjanNode) edge.getDestination());
		}
		for (IEdge edge : node.getIncomingEdges()) {
			neighbors.add((TarjanNode) edge.getDestination());
		}
		return neighbors;
	}
	
}