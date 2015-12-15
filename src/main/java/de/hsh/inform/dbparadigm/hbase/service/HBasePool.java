package de.hsh.inform.dbparadigm.hbase.service;

import java.io.IOException;
import java.util.List;

import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class HBasePool {
	private Connection connection;

	public HBasePool(Connection connection){
		this.connection = connection;
	}

	public void save(IEdge edge) throws IOException{
		Table table = connection.getTable(TableName.valueOf("comment"));
		Put p = new Put(Bytes.toBytes("myLittleRow"));
		p.addColumn(Bytes.toBytes("myLittleFamily"), Bytes.toBytes("someQualifier"), Bytes.toBytes("Some Value"));
		table.put(p);
	}

	public void save(INode node){

	}

	public void save(List<IEdge> edges) {

	}

	public void load(INode node) {

	}

	public void load(List<String> node) {

	}

}
