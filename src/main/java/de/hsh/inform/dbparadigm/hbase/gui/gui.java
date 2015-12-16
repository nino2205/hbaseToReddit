package de.hsh.inform.dbparadigm.hbase.gui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;
import org.graphstream.ui.graphicGraph.GraphicGraph;

/**
 * Created by Sören on 16.12.2015.
 */
public class gui {
    public static void main(String[] args) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        Graph graph = new SingleGraph("Tutorial 1");
        graph.addAttribute("ui.stylesheet",
                "node { z-index: 2; size-mode: fit; shape: rounded-box; stroke-mode: plain; padding: 3px, 2px; text-alignment: center; text-size: 12px;}");

        graph.addNode("A").setAttribute("label","A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B", true);
        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);

        graph.display();
    }
}
