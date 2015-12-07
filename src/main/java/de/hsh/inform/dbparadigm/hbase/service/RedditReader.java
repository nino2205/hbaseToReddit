package de.hsh.inform.dbparadigm.hbase.service;

import de.hsh.inform.dbparadigm.hbase.model.Author;
import de.hsh.inform.dbparadigm.hbase.model.Comment;
import org.apache.commons.collections.MapUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import redditOAuth.RedditOAuth;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

public class RedditReader extends Thread {

    private HashMap<String, Author> authors = new HashMap<>();
    private HashMap<String, Comment> comments = new HashMap<>();

    /**
     * Settings
     */
    private static final int MAX_ROUNDS = 1;

    private static final String LISTING_ARGUMENTS = "?limit=1&after=";

    private String lastFullname = "";

    private String restRequestPath;

    private boolean running = false;


    public RedditReader(String restRequestPath) {
        this.restRequestPath = restRequestPath;
    }

    public void setRestRequestPath(String restRequestPath) {
        if (!this.running) {
            this.restRequestPath = restRequestPath;
        }
    }

    @Override
    public void run() {
        running = true;
        try {
            String curCode = new Long(System.currentTimeMillis()).toString();
            int round = 0;
            boolean done = false;
            Queue<String> idQueue = new ArrayDeque<String>();
            Iterator<Object> postIt = null;
            JSONObject accessToken = RedditOAuth.getToken(curCode);

            // read exists posts
            while (!done) {
                JSONObject response = null;
                response = RedditOAuth.getObject(RedditOAuth.OAUTH_API_DOMAIN + restRequestPath + LISTING_ARGUMENTS + lastFullname, accessToken.get("access_token").toString());

                round++;
                JSONArray posts = response.getJSONObject("data").getJSONArray("children");
                postIt = posts.iterator();
                if (!postIt.hasNext())
                    done = true;
                if (round >= MAX_ROUNDS)
                    done = true;
                while (postIt.hasNext()) {
                    JSONObject cur = (JSONObject) postIt.next();
                    String docId = cur.getJSONObject("data").getString("id");
                    lastFullname = cur.getJSONObject("data").getString("name");
                    //idQueue.add(docId);
                }

            }

            idQueue.add("3u25g5");

            // read the comments, posts and authors
            for (String curId : idQueue) {
                JSONArray arrResponse = RedditOAuth.getArray(RedditOAuth.OAUTH_API_DOMAIN + "/comments/" + curId, accessToken.get("access_token").toString());
                postIt = arrResponse.iterator();
                while (postIt.hasNext()) {
                    Iterator<Object> comIt = ((JSONObject) postIt.next()).getJSONObject("data").getJSONArray("children").iterator();
                    while (comIt.hasNext()) {
                        JSONObject cur = (JSONObject) comIt.next();
                        if( !cur.getString("kind").equals("more") ) {
                            JSONObject data = (JSONObject) cur.getJSONObject("data");

                            String authorId = data.getString("author");
                            Long created = new Long(data.getInt("created"));
                            Author author = null;

                            if (authors.containsKey(authorId)) {
                                author = authors.get(authorId);
                                author.setLastActivity(Math.max(author.getLastActivity(), created));
                            } else {
                                author = new Author(authorId, created);
                                authors.put(authorId, author);
                            }

                            Comment comment = new Comment(null, created);

                            if (data.has("title")) {
                                comment.setTitle(data.getString("title"));
                            } else {
                                comment.setTitle(data.getString("body"));
                            }
                            comments.put(data.getString("name"), comment);
                        }
                    }
                }
                System.out.println(authors.size());
                MapUtils.debugPrint(System.out, "authors", authors);
                System.out.println(comments.size());
                MapUtils.debugPrint(System.out, "comments", comments);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
    }
}
