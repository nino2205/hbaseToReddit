package de.hsh.inform.dbparadigm.hbase.main;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;
import de.hsh.inform.dbparadigm.hbase.service.HBaseConnection;
import de.hsh.inform.dbparadigm.hbase.service.HBasePool;
import de.hsh.inform.dbparadigm.hbase.service.RedditReader;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunHBaseQueries {

    private static Logger logger = Logger.getGlobal();

    private static HBaseConnection connection = null;

    private static HBasePool pool = null;

    public static void main(String[] args) {
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
                case "test":
                    test();
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

    private static void bridge(){

    }

    private static void degree(String nodeId){

    }

    private static void maxDegree(){

    }

    private static void foaf(String nodeId){

    }

    private static void test(){

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
