package de.hsh.inform.dbparadigm.hbase.tarjan;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.hsh.inform.dbparadigm.hbase.model.Edge;
import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;
import de.hsh.inform.dbparadigm.hbase.model.Node;

public class TarjanTest {

	public static void main(String[] args) {
		List<INode> nodes = buildGraph();
		Algorithm algorithm = new Algorithm(nodes);
		Collection<Collection<INode>> result = algorithm.execute();
		
		System.out.println(result.size() == 2);
		System.out.println(result);
	}
	
	/**
	 * Building a simple graph with two SCC 
	 * (strongly connected components).
	 * 
	 *       n1          n5
	 *      /  \        / \
	 *     /    \      /   \
	 *    n2----n3----n4----n6
	 *    
	 */
	private static LinkedList<INode> buildGraph () {
		LinkedList<INode> nodes = new LinkedList<>();
	    Node n1 = new Node("n1", "n1", 1L);
	    Node n2 = new Node("n2", "n2", 2L);
	    Node n3 = new Node("n3", "n3", 3L);
	    Node n4 = new Node("n4", "n4", 4L);
	    Node n5 = new Node("n5", "n5", 5L);
	    Node n6 = new Node("n6", "n6", 6L);

	    nodes.add(n1);
	    nodes.add(n2);
	    nodes.add(n3);
	    nodes.add(n4);
	    nodes.add(n5);
	    nodes.add(n6);
	    
	    // Interconnected
	    Edge e12 = new Edge("e12", "e12", 12L, n1, n2);
	    Edge e23 = new Edge("e23", "e23", 23L, n2, n3);
	    Edge e31 = new Edge("e31", "e31", 31L, n3, n1);
	    
	    // Interconnected
	    Edge e45 = new Edge("e45", "e45", 45L, n4, n5);
	    Edge e56 = new Edge("e56", "e56", 56L, n5, n6);
	    Edge e64 = new Edge("e64", "e64", 64L, n6, n4);
	    
	    // Connect components (This should not effect them)
	    Edge e34 = new Edge("e34", "e34", 34L, n3, n4);
	    
	    LinkedList<IEdge> n1In = new LinkedList<IEdge>();
	    LinkedList<IEdge> n2In = new LinkedList<IEdge>();
	    LinkedList<IEdge> n3In = new LinkedList<IEdge>();
	    LinkedList<IEdge> n4In = new LinkedList<IEdge>();
	    LinkedList<IEdge> n5In = new LinkedList<IEdge>();
	    LinkedList<IEdge> n6In = new LinkedList<IEdge>();
	    
	    LinkedList<IEdge> n1Out = new LinkedList<IEdge>();
	    LinkedList<IEdge> n2Out = new LinkedList<IEdge>();
	    LinkedList<IEdge> n3Out = new LinkedList<IEdge>();
	    LinkedList<IEdge> n4Out = new LinkedList<IEdge>();
	    LinkedList<IEdge> n5Out = new LinkedList<IEdge>();
	    LinkedList<IEdge> n6Out = new LinkedList<IEdge>();

	    n1In.add(e31);
	    n2In.add(e12);
	    n3In.add(e23);
	    n4In.add(e64); n4In.add(e34);
	    n5In.add(e45);
	    n6In.add(e56);
	    
	    n1Out.add(e12);
	    n2Out.add(e23);
	    n3Out.add(e31); n3Out.add(e34);
	    n4Out.add(e45);
	    n5Out.add(e56);
	    n6Out.add(e64);

	    n1.setIncomingEdges(n1In);
	    n2.setIncomingEdges(n2In);
	    n3.setIncomingEdges(n3In);
	    n4.setIncomingEdges(n4In);
	    n5.setIncomingEdges(n5In);
	    n6.setIncomingEdges(n6In);
	    
	    n1.setOutgoingEdges(n1Out);
	    n2.setOutgoingEdges(n2Out);
	    n3.setOutgoingEdges(n3Out);
	    n4.setOutgoingEdges(n4Out);
	    n5.setOutgoingEdges(n5Out);
	    n6.setOutgoingEdges(n6Out);
	    
	    return nodes;
	}
	
}
