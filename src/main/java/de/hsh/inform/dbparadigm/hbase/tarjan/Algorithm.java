package de.hsh.inform.dbparadigm.hbase.tarjan;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import de.hsh.inform.dbparadigm.hbase.model.INode;

public class Algorithm {

	// The nodes to traverse.
	private final List<INode> nodes;
	
	// Ordered Set of visited nodes.
	private final Stack<INode> stack;
	
	// The strongly connected components (SCC).
	private List<List<INode>> components;
	
	// Used to assign unique incremental indicies. 
	private int pointer;
	
	// Used to store the indicies, links and visited status.
	private final HashMap<INode, Integer> ids;
	private final HashMap<INode, Integer> links;
	private final HashMap<INode, Boolean> visited;
	
	/**
	 * Constuctor.
	 */
	public Algorithm (List<INode> nodes) {
		this.nodes = nodes;
		this.stack = new Stack<>();
		this.components = new Vector<>();
		this.pointer = 0;

		this.ids = new HashMap<>();
		this.links = new HashMap<>();
		this.visited = new HashMap<>();
		
		for (INode node: this.nodes) {
			this.ids.put(node, -1);
			this.links.put(node, -2);
			this.visited.put(node, false);
		}
	}
	
	/**
	 * Traverses all unvisited INodes of nodes and returns the strong 
	 * components of this.nodes as a Collection of INode Collections.
	 */
	public List<List<INode>> execute () {
		int size = this.nodes.size();
		this.pointer = 0;
		this.components = new Vector<>();
		
		for (int i = 0; i < size; i++) {
			INode node = this.nodes.get(i);
			if (!this.visited.get(node)) this.visit(node);
		}
		return components;
	}
	
	/**
	 * Visits the INode node and it's unvisited neighbors. If the
	 * link of the node equals it's index, a SCC is found.
	 */
	private void visit (INode node) {
		this.ids.put(node, pointer);
		this.links.put(node, pointer);
		this.visited.put(node, true);
		this.stack.push(node);
	    this.pointer += 1;
	    
	    for (INode neighbour : node.getNeighbours()) {
	    	if (nodes.contains(neighbour) && !this.visited.get(neighbour)) {
	    		this.visit(neighbour);
	    		this.adjustLink(node, this.links.get(neighbour));
		    } else if (stack.contains(neighbour)) {
	    		this.adjustLink(node, this.ids.get(neighbour));
	    	}
	    }
	    if (links.get(node).equals(ids.get(node))) {
	    	this.components.add(this.createComponent(node));
	    }
	}
	
	/**
	 * Sets the link of the INode node to value, if it's smaller
	 * than the current link value.
	 */
	private void adjustLink (INode node, Integer value) {
		if (value < this.links.get(node)) {
			this.links.put(node, value);
		}
	}
	
	/**
	 * Creates a Component (Vector) from all INodes on the stack 
	 * until the INode node is reached (inclusively).
	 */
	private Vector<INode> createComponent (INode node) {
		Vector<INode> component = new Vector<INode>();
		
		while (true) {
			INode current = stack.pop();
			component.add(current);
			if (node.equals(current)) break;
		}
		return component;
	}
	
}