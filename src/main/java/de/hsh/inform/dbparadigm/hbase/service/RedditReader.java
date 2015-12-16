package de.hsh.inform.dbparadigm.hbase.service;

import de.hsh.inform.dbparadigm.hbase.model.*;
import org.apache.commons.collections.MapUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import redditOAuth.RedditOAuth;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

public class RedditReader{

    public HashMap<String, INode> nodes = new HashMap<>();
    public HashMap<String, IEdge> edge = new HashMap<>();
    private HashMap<String, INode> article2author = new HashMap<>();

    /**
     * Settings
     */
    private static final int MAX_ROUNDS = 1;

    private static final String LISTING_ARGUMENTS = "?limit=100&after=";

    private String lastFullname = "";

    private String restRequestPath;


    public RedditReader(String restRequestPath) {
        this.restRequestPath = restRequestPath;
    }

    public void setRestRequestPath(String restRequestPath) {
        this.restRequestPath = restRequestPath;
    }

    public void run() {
        try {
            String curCode = new Long(System.currentTimeMillis()).toString();
            int round = 0;
            boolean done = false;
            Queue<String> idQueue = new ArrayDeque<String>();
            Iterator<Object> postIt = null;
            JSONObject accessToken = RedditOAuth.getToken(curCode);

            // read subreddit
            JSONObject response = null;
            response = RedditOAuth.getObject(RedditOAuth.OAUTH_API_DOMAIN + restRequestPath + "/about", accessToken.get("access_token").toString());

            SubReddit subReddit = new SubReddit(
                    response.getJSONObject("data").getString("name"),
                    response.getJSONObject("data").getString("title"),
                    response.getJSONObject("data").getLong("created")
            );

            nodes.put(subReddit.getIdentifierString(), subReddit);

            // read exists articles
            while (!done) {
                response = null;
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

                    Author author = new Author(
                            cur.getJSONObject("data").getString("author"),
                            cur.getJSONObject("data").getString("author"),
                            cur.getJSONObject("data").getLong("created")
                    );

                    Article article = new Article(
                            docId,
                            cur.getJSONObject("data").getString("title"),
                            cur.getJSONObject("data").getLong("created"),
                            subReddit,
                            author
                    );

                    nodes.put(author.getIdentifierString(), author);
                    edge.put(article.getIdentifierString(), article);
                    article2author.put(docId, author);

                    System.out.println(cur);
                    idQueue.add(docId);
                }

            }

            // read the edge authors
            for (String curId : idQueue) {
                JSONArray arrResponse = RedditOAuth.getArray(RedditOAuth.OAUTH_API_DOMAIN + "/comments/" + curId, accessToken.get("access_token").toString());
                postIt = arrResponse.iterator();
                while (postIt.hasNext()) {
                    Iterator<Object> comIt = ((JSONObject) postIt.next()).getJSONObject("data").getJSONArray("children").iterator();
                    readReplies(article2author.get(curId), comIt);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readReplies(INode parent, Iterator<Object> comIt) {
        while (comIt.hasNext()) {
            JSONObject cur = (JSONObject) comIt.next();

            if (!cur.getString("kind").equals("more")) {
                JSONObject data = (JSONObject) cur.getJSONObject("data");

                String authorId = data.getString("author");
                Long created = data.getLong("created");
                INode author = null;

                if (nodes.containsKey(authorId)) {
                    author = nodes.get(authorId);
                    author.setLastActivity(Math.max(author.getLastActivity(), created));
                } else {
                    author = new Author(authorId, authorId, created);
                    nodes.put(authorId, author);
                }

                IEdge comment = new Comment(data.getString("name"), null, created, parent, author);
                parent.addOutgoingEdges(comment);
                author.addIncomingEdges(comment);

                if (data.has("title")) {
                    comment.setTitle(data.getString("title"));
                } else {
                    comment.setTitle(data.getString("body"));
                }
                edge.put(comment.getIdentifierString(), comment);

                if (data.has("replies") && data.get("replies") instanceof JSONObject) {
                    readReplies(author, data.getJSONObject("replies").getJSONObject("data").getJSONArray("children").iterator());
                }
            } else {
                // TODO: es muss bei more elementen weiter gemacht werden
                System.out.println("es muss bei more elementen weiter gemacht werden");
                System.out.println(cur);
                //System.exit(-1);
            }
        }
    }
}
