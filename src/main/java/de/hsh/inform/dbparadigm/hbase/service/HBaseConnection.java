package de.hsh.inform.dbparadigm.hbase.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseConnection {
    private Configuration config;
    private Connection connection;

    public HBaseConnection() throws IOException {
        System.out.println("Trying to connect...");

        config = HBaseConfiguration.create();

        System.out.println("HBase is running!");

        connection = ConnectionFactory.createConnection(config);

        createTable(connection);

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

    private void createTable(Connection connection) throws IOException{
        Admin admin = connection.getAdmin();
        System.out.println(admin);
        HTableDescriptor tableDescriptor = new HTableDescriptor(new HTableDescriptor(TableName.valueOf("test_table")));
        tableDescriptor.addFamily(new HColumnDescriptor("test_column_family").setCompressionType(Compression.Algorithm.SNAPPY));
        System.out.println("Creating table");
        createOrOverwrite(admin,tableDescriptor);
    }

    private void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }
}
