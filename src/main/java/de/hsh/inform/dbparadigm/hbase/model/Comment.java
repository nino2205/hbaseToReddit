package de.hsh.inform.dbparadigm.hbase.model;

/**
 * Created by Sören on 07.12.2015.
 */
public class Comment extends Edge {
    public Comment(String identifier, String title, Long created, INode source, INode destination) {
        super(identifier, title, created, source, destination);
    }
}
