package de.hsh.inform.dbparadigm.hbase.model;

/**
 * Created by Sören on 08.12.2015.
 */
public class Article extends Edge {
    public Article(String identifier, String title, Long created, INode source, INode destination) {
        super(identifier, title, created, source, destination);
    }
}
