package de.hsh.inform.dbparadigm.hbase.tarjan;

import java.util.Collection;
import java.util.LinkedList;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;

public class TarjanTest {

	public static void main(String[] args) {
		LinkedList<TarjanNode> nodes = buildGraph();
		Algorithm algorithm = new Algorithm(nodes);
		Collection<Collection<TarjanNode>> result = algorithm.execute();
		
		System.out.println(result.size() == 2);
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
	private static LinkedList<TarjanNode> buildGraph () {
		LinkedList<TarjanNode> nodes = new LinkedList<>();
	    TarjanNode n1 = new TarjanNode("n1");
	    TarjanNode n2 = new TarjanNode("n2");
	    TarjanNode n3 = new TarjanNode("n3");
	    TarjanNode n4 = new TarjanNode("n4");
	    TarjanNode n5 = new TarjanNode("n5");
	    TarjanNode n6 = new TarjanNode("n6");

	    nodes.add(n1);
	    nodes.add(n2);
	    nodes.add(n3);
	    nodes.add(n4);
	    nodes.add(n5);
	    nodes.add(n6);
	    
	    // Interconnected
	    TarjanEdge e12 = new TarjanEdge("e12", n1, n2);
	    TarjanEdge e23 = new TarjanEdge("e23", n2, n3);
	    TarjanEdge e31 = new TarjanEdge("e31", n3, n1);
	    
	    // Interconnected
	    TarjanEdge e45 = new TarjanEdge("e45", n4, n5);
	    TarjanEdge e56 = new TarjanEdge("e56", n5, n6);
	    TarjanEdge e64 = new TarjanEdge("e64", n6, n4);
	    
	    // Connect components (This should not effect them)
	    TarjanEdge e34 = new TarjanEdge("34", n3, n4);
	    
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
