package de.hsh.inform.dbparadigm.hbase.main;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;
import de.hsh.inform.dbparadigm.hbase.service.HBaseConnection;
import de.hsh.inform.dbparadigm.hbase.service.HBasePool;
import de.hsh.inform.dbparadigm.hbase.service.RedditReader;
import de.hsh.inform.dbparadigm.hbase.tarjan.Algorithm;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunHBaseQueries {

    private static Logger logger = Logger.getGlobal();

    private static HBaseConnection connection = null;

    private static HBasePool pool = null;

    public static void main(String[] args) throws IOException{
        try {
            connection = HBaseConnection.getInstance();
            pool = new HBasePool(connection.getConnection());
        } catch (IOException e) {
            logger.log(Level.WARNING, "error by open connection to hbase");
            System.exit(-1);
        }

        String[] command = null;
        menu();
        do {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine().toLowerCase().trim().split(" ");

            switch (command[0]){
                case "get":
                    if( command.length == 2 ){
                        get(command[1]);
                    } else {
                        System.out.println("we need a subreddit");
                    }
                    break;
                case "bridge":
                    bridge();
                    break;
                case "degree":
                    if( command.length == 2 ){
                        degree(command[1]);
                    } else {
                        System.out.println("we need a nodeID");
                    }
                    break;
                case "maxdegree":
                    maxDegree();
                    break;
                case "foaf":
                    if( command.length == 2 ){
                        foaf(command[1]);
                    } else {
                        System.out.println("we need a nodeID");
                    }
                    break;
                case "delete":
                    try {
                        pool.deleteTables();
                        System.out.println("deleted tables");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "create":
                    try {
                        pool.createTables();
                        System.out.println("created tables");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "gui":
                    gui();
                    break;
                case "exit":
                    break;
                default:
                    menu();
            }
        } while( !command[0].equals("exit") );

    }

    private static void get(String subreddit){
        RedditReader reader = new RedditReader(subreddit);
        reader.run();
        List<IEdge> edges = new ArrayList<IEdge>(reader.edge.values());
        List<INode> nodes = new ArrayList<INode>(reader.nodes.values());
        HBasePool pool = new HBasePool(connection.getConnection());
        try {
            pool.saveEdges(edges);
            pool.saveNodes(nodes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void bridge() throws IOException{
        HashMap<String, IEdge> comments = pool.scanComment();
        HashMap<String, INode> nodes = new HashMap<>();

        for (String key: comments.keySet()) {
            if(!nodes.containsKey( comments.get(key).getSource().getIdentifierString() )){
                nodes.put( comments.get(key).getSource().getIdentifierString(), comments.get(key).getSource() );
            }

            if(!nodes.containsKey( comments.get(key).getDestination().getIdentifierString() )){
                nodes.put( comments.get(key).getDestination().getIdentifierString(), comments.get(key).getDestination() );
            }
        }

        Algorithm tarjan = new Algorithm((List<INode>) nodes.values());

        final long timeStart = System.currentTimeMillis();
        List<List<INode>> comp = tarjan.execute();
        final long timeEnd = System.currentTimeMillis();

        System.out.println("components: " + comp.size() + " in " + (timeEnd - timeStart) + " ms");

        Graph graph = new SingleGraph("Tutorial 1");
        graph.addAttribute("ui.stylesheet",
                "node { z-index: 2; size-mode: fit; shape: rounded-box; stroke-mode: plain; padding: 3px, 2px; text-alignment: center; text-size: 12px;}");

        for (List<INode> iNodes : comp) {
            for (INode iNode : iNodes) {
                for (IEdge iEdge : iNode.getIncomingEdges()) {
                    bridgeGraph(graph, iEdge);
                }
                for (IEdge iEdge : iNode.getOutgoingEdges()) {
                    bridgeGraph(graph, iEdge);
                }
            }
        }
    }

    private static void bridgeGraph(Graph graph, IEdge iEdge){
        if( graph.getNode( iEdge.getSource().getIdentifierString() ) == null ) {
            graph.addNode(iEdge.getSource().getIdentifierString())
                    .addAttribute("label", iEdge.getSource().getIdentifierString());
        }

        if( graph.getNode( iEdge.getDestination().getIdentifierString() ) == null ) {
            graph.addNode( iEdge.getDestination().getIdentifierString())
                    .addAttribute("label", iEdge.getDestination().getIdentifierString());
        }

        graph.addEdge(
                iEdge.getSource().getIdentifierString()+iEdge.getDestination().getIdentifierString(),
                iEdge.getSource().getIdentifierString(),
                iEdge.getDestination().getIdentifierString(),
                true
        );
    }

    private static void degree(String nodeId) throws IOException{
        ValueFilter vfOut = new ValueFilter(
                CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(nodeId + "//|*")
        );
        ValueFilter vfIn = new ValueFilter(
                CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(nodeId + "//|*")
        );

        Scan scan = new Scan();

        scan.setFilter(vfOut);
        ResultScanner vfInresults = pool.getCommentTable().getScanner(scan);

        scan.setFilter(vfIn);
        ResultScanner vfOutresults = pool.getCommentTable().getScanner(scan);
    }

    private static void maxDegree(){

    }

    private static void foaf(String nodeId){

    }

    private static void gui(){
        try {
            HashMap<String, IEdge> edges = pool.scanComment();

            Graph graph = new SingleGraph("Tutorial 1");
            graph.addAttribute("ui.stylesheet",
                    "node { z-index: 2; size-mode: fit; shape: rounded-box; stroke-mode: plain; padding: 3px, 2px; text-alignment: center; text-size: 12px;}");

            for(Map.Entry<String, IEdge> entry : edges.entrySet()){
                if( graph.getNode( entry.getValue().getSource().getIdentifierString() ) == null ) {
                    graph.addNode(entry.getValue().getSource().getIdentifierString())
                            .addAttribute("label", entry.getValue().getSource().getIdentifierString());
                }

                if( graph.getNode( entry.getValue().getDestination().getIdentifierString() ) == null ) {
                    graph.addNode(entry.getValue().getDestination().getIdentifierString())
                            .addAttribute("label", entry.getValue().getDestination().getIdentifierString());
                }

                graph.addEdge(
                        entry.getValue().getSource().getIdentifierString()+entry.getValue().getDestination().getIdentifierString(),
                        entry.getValue().getSource().getIdentifierString(),
                        entry.getValue().getDestination().getIdentifierString(),
                        true
                        );
            }

            graph.display();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void menu(){
        System.out.println("Welcome to RedditToHbase");
        System.out.println("-----------------------");
        System.out.println("get <subreddit>         - get the subreddit and added to hbase");
        System.out.println("bridge                  - bridge with tarjan's algorithm");
        System.out.println("degree <NodeID>         - DegreeCentrality");
        System.out.println("maxdegree               - MaxDegree");
        System.out.println("foaf <NodeID>           - Friends of a Friend");
        System.out.println("");
        System.out.println("delete                  - delete all tables");
        System.out.println("create                  - create all tables");
        System.out.println("");
        System.out.println("exit");
        System.out.println("-----------------------");
    }

}
