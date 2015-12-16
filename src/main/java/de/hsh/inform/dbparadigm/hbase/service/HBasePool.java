package de.hsh.inform.dbparadigm.hbase.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hsh.inform.dbparadigm.hbase.model.Author;
import de.hsh.inform.dbparadigm.hbase.model.Comment;
import de.hsh.inform.dbparadigm.hbase.model.IEdge;
import de.hsh.inform.dbparadigm.hbase.model.INode;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
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

	public void save(INode node) throws IOException{
		getAuthorTable().put(createNodePut(node));
	}

	public void saveNodes(List<INode> nodes) throws IOException{
		ArrayList<Put> puts = new ArrayList<>();
		for (INode n:nodes) {
			puts.add(createNodePut(n));
		}
		getAuthorTable().put(puts);
	}

	public void saveEdges(List<IEdge> edges) throws IOException{
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

	public HashMap<String, INode> scanAuthor() throws IOException {
		HashMap<String, INode> nodes = new HashMap<>();
		Scan scan = new Scan();
		ResultScanner results = getAuthorTable().getScanner(scan);

		for (Result result = results.next(); result != null; result = results.next()) {
			String key = Bytes.toString(result.getRow());
			String value = Bytes.toString(result.getValue(Bytes.toBytes("properties"), Bytes.toBytes("userid")));
			Long timestamp = result.rawCells()[0].getTimestamp();
			nodes.put(key, new Author(key, value, timestamp));
		}
		return nodes;
	}

	public HashMap<String, IEdge> scanComment() throws IOException{
		HashMap<String, IEdge> edges = new HashMap<>();
		HashMap<String, INode> nodes = scanAuthor();
		Scan scan = new Scan();
		ResultScanner results = getCommentTable().getScanner(scan);

		for (Result result = results.next(); result != null; result = results.next()) {
			String id = Bytes.toString(result.getRow());
			String[] authors = Bytes.toString(result.getRow()).split("\\|");
			String value = Bytes.toString(result.getValue(Bytes.toBytes("properties"), Bytes.toBytes("title")));
			Long timestamp = result.rawCells()[0].getTimestamp();

			INode source = nodes.get(authors[0]);
			INode destination = nodes.get(authors[1]);

			Comment comment = new Comment(id, value, timestamp, source, destination);
			source.addOutgoingEdges(comment);
			destination.addIncomingEdges(comment);

			edges.put(id, comment);
		}

		return edges;
	}

	public void deleteTables() throws IOException{
		Admin admin = connection.getAdmin();
		deleteTable(admin, getAuthorTable().getName());
		deleteTable(admin, getCommentTable().getName());
	}

	private void deleteTable(Admin admin, TableName tableName) throws IOException{
		if (admin.tableExists(tableName)) {
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
		}
	}

	private Put createEdgePut(IEdge edge){
		Put p = new Put(Bytes.toBytes(edge.getSource().getIdentifierString() + "|" + edge.getDestination().getIdentifierString()));
		p.addColumn(Bytes.toBytes("properties"), Bytes.toBytes("title"), edge.getCreated(), Bytes.toBytes(edge.getTitle()));
		return p;
	}

	private Put createNodePut(INode node){
		Put p = new Put(Bytes.toBytes(node.getIdentifierString()));
		p.addColumn(Bytes.toBytes("properties"), Bytes.toBytes("userid"), node.getLastActivity(), Bytes.toBytes(node.getIdentifierString()));
		return p;
	}

	private Table getCommentTable() throws IOException{
		if( commentTable == null ){
			commentTable = connection.getTable(TableName.valueOf("comment"));
		}
		return commentTable;
	}

	private Table getAuthorTable() throws IOException{
		if( authorTable == null ){
			authorTable = connection.getTable(TableName.valueOf("author"));
		}
		return authorTable;
	}

	public void createTables() throws IOException {
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
