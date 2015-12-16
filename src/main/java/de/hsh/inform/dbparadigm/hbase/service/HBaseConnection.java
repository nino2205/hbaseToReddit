package de.hsh.inform.dbparadigm.hbase.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;

import java.io.IOException;

public class HBaseConnection {
    private static HBaseConnection instance = null;
    private Configuration config;
    private Connection connection;

    public static HBaseConnection getInstance() throws IOException {
        if(instance == null){
            instance = new HBaseConnection();
        }
        return instance;
    }

    protected HBaseConnection() throws IOException {
        System.out.println("Trying to connect...");

        config = HBaseConfiguration.create();

        System.out.println("HBase is running!");

        connection = ConnectionFactory.createConnection(config);
    }

    public Connection getConnection(){
        return connection;
    }
}
