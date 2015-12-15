package de.hsh.inform.dbparadigm.hbase.service;

import java.io.IOException;
import java.util.ArrayList;
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
	private Table commentTable = null;
	private Table authorTable = null;

	public HBasePool(Connection connection){
		this.connection = connection;
	}

	public void save(IEdge edge) throws IOException{
		getCommentTable().put(createEdgePut(edge));
	}

	public void save(INode node){

	}

	public void save(List<IEdge> edges) throws IOException{
		ArrayList<Put> puts = new ArrayList<>();
		for (IEdge e:edges) {
			puts.add(createEdgePut(e));
		}
		getCommentTable().put(puts);
	}

	public void load(INode node) {

	}

	public void load(List<String> node) {

	}

	private Put createEdgePut(IEdge edge){
		Put p = new Put(Bytes.toBytes(edge.getSource().getIdentifierString() + "|" + edge.getDestination().getIdentifierString()));
		p.addColumn(Bytes.toBytes("properties"), Bytes.toBytes(edge.getIdentifierString()), edge.getCreated(), Bytes.toBytes(edge.getTitle()));
		return p;
	}

	private Table getCommentTable() throws IOException{
		if( commentTable == null ){
			commentTable = connection.getTable(TableName.valueOf("comment"));
		}
		return commentTable;
	}

	private Table getAuthorTable() throws IOException{
		if( commentTable == null ){
			commentTable = connection.getTable(TableName.valueOf("author"));
		}
		return commentTable;
	}

}
