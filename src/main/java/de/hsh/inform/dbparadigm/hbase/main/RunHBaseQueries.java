package de.hsh.inform.dbparadigm.hbase.main;

import de.hsh.inform.dbparadigm.hbase.service.RedditReader;

public class RunHBaseQueries {

	public static void main(String[] args) {
		RedditReader reader = new RedditReader("/r/de");
		reader.start();
	}

}
