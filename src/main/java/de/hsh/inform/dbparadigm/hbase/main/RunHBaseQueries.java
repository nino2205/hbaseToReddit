package de.hsh.inform.dbparadigm.hbase.main;

import de.hsh.inform.dbparadigm.hbase.service.HBaseConnection;
import de.hsh.inform.dbparadigm.hbase.service.RedditReader;
import de.hsh.inform.dbparadigm.hbase.service.query.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunHBaseQueries {

    private static Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        Properties config = null;
        try {
            config = RunHBaseQueries.readConfigFile("config.properties");
        } catch (IOException e) {
            logger.log(Level.WARNING, "error by reading properties file");
            System.exit(-1);
        }

        try {
            HBaseConnection connection = new HBaseConnection(
                    config.getProperty("hbase.zookeeper.quorum"),
                    config.getProperty("hbase.zookeeper.property.clientPort"),
                    config.getProperty("hbase.master")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


        RedditReader reader = new RedditReader(config.getProperty("subreddit"));
        reader.start();


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
