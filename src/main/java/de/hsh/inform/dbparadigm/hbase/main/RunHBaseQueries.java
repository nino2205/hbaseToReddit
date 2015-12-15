package de.hsh.inform.dbparadigm.hbase.main;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.service.HBaseConnection;
import de.hsh.inform.dbparadigm.hbase.service.HBasePool;
import de.hsh.inform.dbparadigm.hbase.service.RedditReader;
import de.hsh.inform.dbparadigm.hbase.service.query.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunHBaseQueries {

    private static Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        Properties config = null;
        HBaseConnection connection = null;

        try {
            config = RunHBaseQueries.readConfigFile("properties/config.properties");
        } catch (IOException e) {
            logger.log(Level.WARNING, "error by reading properties file");
            System.exit(-1);
        }

        try {
            connection = HBaseConnection.getInstance();
        } catch (IOException e) {
            logger.log(Level.WARNING, "no connection to hbase");
            System.exit(-1);
        }

        RedditReader reader = new RedditReader(config.getProperty("subreddit"));
        HashMap<String, IEdge> edges = reader.run();

        Iterator iterator = edges.entrySet().iterator();
        HBasePool hBasePool = new HBasePool(connection.getConnection());

        IEdge edge = null;
        while ( iterator.hasNext() ){
            try {
                edge = (IEdge) iterator.next();
                hBasePool.save(edge);
            } catch (IOException e) {
                logger.log(Level.WARNING, edge.toString());
            }
        }


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

    public static Properties readConfigFile(String filename) throws IOException {
        Properties properties = new Properties();
        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filename));
        properties.load(stream);
        stream.close();
        return properties;
    }

}
