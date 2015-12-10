package de.hsh.inform.dbparadigm.hbase.service;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseConnection {
    private Configuration configuration;
    private Connection connection;

    public HBaseConnection() throws IOException {
        System.out.println("Trying to connect...");

        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "hortonworks.hbase.vm");
        configuration.set("zookeeper.znode.parent", "/hbase-unsecure");

        System.out.println("HBase is running!");

        connection = ConnectionFactory.createConnection(configuration);

        Table table = connection.getTable(TableName.valueOf("myLittleHBaseTable"));
        Put p = new Put(Bytes.toBytes("myLittleRow"));
        p.add(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"), Bytes.toBytes("Some Value"));
        table.put(p);

        Get g = new Get(Bytes.toBytes("myLittleRow"));
        Result r = table.get(g);
        byte [] value = r.getValue(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"));

        // If we convert the value bytes, we should get back 'Some Value', the
        // value we inserted at this location.
        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
    }
}
