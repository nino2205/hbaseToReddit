package de.hsh.inform.dbparadigm.hbase.service;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseConnection{
    public HBaseConnection(String hbaseZookeeperQuorum, String hbaseZookeeperPropertyClientPort, String hbaseMaster) throws IOException {
        Configuration config = HBaseConfiguration.create();

        config.clear();
        config.set("hbase.zookeeper.quorum", hbaseZookeeperQuorum);
        config.set("hbase.zookeeper.property.clientPort",hbaseZookeeperPropertyClientPort);
        config.set("hbase.master", hbaseMaster);

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (ServiceException e) {
            System.out.println("hbase not available");
            e.printStackTrace();
        }

        Connection connection = ConnectionFactory.createConnection(config);

        Table table = connection.getTable(TableName.valueOf("test"));

        Put p = new Put(Bytes.toBytes("myLittleRow"));

        p.add(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"), Bytes.toBytes("Some Value"));
    }
}
