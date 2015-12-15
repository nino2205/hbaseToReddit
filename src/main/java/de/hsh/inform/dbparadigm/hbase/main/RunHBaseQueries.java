package de.hsh.inform.dbparadigm.hbase.main;

import de.hsh.inform.dbparadigm.hbase.service.HBaseConnection;
import de.hsh.inform.dbparadigm.hbase.service.RedditReader;
import de.hsh.inform.dbparadigm.hbase.service.query.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunHBaseQueries {

    private static Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        try {
            HBaseConnection connection = new HBaseConnection();
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
                        RedditReader reader = new RedditReader(command[1]);
                        reader.start();
                    } else {System.out.println("we need a subreddit");}
                    break;
                case "bridge":
                    System.out.println("computing bridge");
                    break;
                case "degree":
                    if( command.length == 2 ){
                        System.out.println("computing degree");
                    } else {System.out.println("we need a nodeID");}
                    break;
                case "maxdegree":
                    System.out.println("computing maxdegree");
                    break;
                case "foaf":
                    if( command.length == 2 ){
                        System.out.println("computing friends of a friend");
                    } else {System.out.println("we need a nodeID");}
                    break;
                case "exit":
                    break;
                default:
                    menu();
            }
        } while( !command[0].equals("exit") );

    }

    private static void menu(){
        System.out.println("Welcome to Reddit2Hbase");
        System.out.println("-----------------------");
        System.out.println("get <subreddit>         - get the subreddit and added to hbase");
        System.out.println("bridge                  - bridge with tarjan's algorithm");
        System.out.println("degree <NodeID>         - DegreeCentrality");
        System.out.println("maxdegree               - MaxDegree");
        System.out.println("foaf <NodeID>           - Friends of a Friend");
        System.out.println("");
        System.out.println("exit");
        System.out.println("-----------------------");
    }

    private static void start(){
        ArrayList<IQuery> queries = new ArrayList<>();
        queries.add(new DegreeCentrality());
        queries.add(new MaxDegree());
        queries.add(new FriendsOfAFriend());
        queries.add(new Bridge());

        long start = 0L;
        for (IQuery query : queries) {
            start = new Date().getTime();
            query.run();
            System.out.println(query.getName() + " in " + (new Date().getTime()-start) + " ms");
        }
    }
}
