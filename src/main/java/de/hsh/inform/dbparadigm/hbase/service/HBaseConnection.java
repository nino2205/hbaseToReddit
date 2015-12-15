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

        if( true ) { // TODO: in der Config eintragen ob erstellt werden soll oder nicht | im menue eine Option machen
            createTables(connection);
        }

        /*
        Table table = connection.getTable(TableName.valueOf("myLittleHBaseTable"));
        Put p = new Put(Bytes.toBytes("myLittleRow"));
        p.addColumn(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"), Bytes.toBytes("Some Value"));
        table.put(p);

        System.out.println("Table!");

        Get g = new Get(Bytes.toBytes("myLittleRow"));
        Result r = table.get(g);
        byte [] value = r.getValue(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"));

        // If we convert the value bytes, we should get back 'Some Value', the
        // value we inserted at this location.
        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
        */
    }

    public Connection getConnection(){
        return connection;
    }

    private void createTables(Connection connection) throws IOException {
        Admin admin = connection.getAdmin();

        HTableDescriptor tableDescriptor = null;

        tableDescriptor = new HTableDescriptor(new HTableDescriptor(TableName.valueOf("comment")));
        tableDescriptor.addFamily(new HColumnDescriptor("properties").setCompressionType(Compression.Algorithm.SNAPPY));
        createOrOverwrite(admin, tableDescriptor);

        tableDescriptor = new HTableDescriptor(new HTableDescriptor(TableName.valueOf("author")));
        tableDescriptor.addFamily(new HColumnDescriptor("properties").setCompressionType(Compression.Algorithm.SNAPPY));
        createOrOverwrite(admin, tableDescriptor);

        tableDescriptor = new HTableDescriptor(new HTableDescriptor(TableName.valueOf("subreddit")));
        tableDescriptor.addFamily(new HColumnDescriptor("author").setCompressionType(Compression.Algorithm.SNAPPY));
        createOrOverwrite(admin, tableDescriptor);
    }

    private void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }
}
