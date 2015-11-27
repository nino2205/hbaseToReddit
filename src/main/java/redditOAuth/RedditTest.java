package redditOAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;


/**
 * Created by Carsten on 27.10.2015.
 */
public class RedditTest {
    private static final int MAX_ROUNDS = 10;

    public static void main(String[] args) {
        String curCode = new Long(System.currentTimeMillis()).toString();
        String restRequestPath = "/r/mobileweb";
        if (args.length>0) {
            restRequestPath = args[0];
        }
        String listingArguments = "?limit=100&after=";
        String lastFullname = "";
        try {
            int round = 0;
            boolean done = false;
            Queue<String> idQueue = new ArrayDeque<String>();
            Iterator<Object> postIt = null;
            JSONObject accessToken = RedditOAuth.getToken(curCode);
            while (!done) {
                JSONObject response = RedditOAuth.getObject(RedditOAuth.OAUTH_API_DOMAIN + restRequestPath + listingArguments + lastFullname, accessToken.get("access_token").toString());
                //FileWriter file = new FileWriter("response" + curCode + ".json");
                //response.write(file);
                round++;
                JSONArray posts = response.getJSONObject("data").getJSONArray("children");
                postIt = posts.iterator();
                if (!postIt.hasNext())
                    done = true;
                if (round>MAX_ROUNDS)
                    done = true;
                while (postIt.hasNext()) {
                    JSONObject cur = (JSONObject) postIt.next();
                    String docId = cur.getJSONObject("data").getString("id");
                    lastFullname = cur.getJSONObject("data").getString("name");
                    idQueue.add(docId);
                }
            }
            System.out.println("Done after "+(round-1)+" rounds, collected "+idQueue.size()+ " IDs.");
            for (String curId : idQueue) {
                JSONArray arrResponse = RedditOAuth.getArray(RedditOAuth.OAUTH_API_DOMAIN + "/comments/" + curId, accessToken.get("access_token").toString() );
                postIt = arrResponse.iterator();
                while (postIt.hasNext()) {
                    Iterator<Object> comIt = ((JSONObject) postIt.next()).getJSONObject("data").getJSONArray("children").iterator();
                    while (comIt.hasNext()) {
                        JSONObject cur = (JSONObject)comIt.next();
                        // Do something reasonable with the comment here, i.e. insert into graph
                        System.out.println("Detected new comment: user Id: "+ cur.getJSONObject("data").getString("author"));
                        if (cur.getJSONObject("data").has("title"))
                        {
                            System.out.println(" Connects to previous user at "+cur.getJSONObject("data").getInt("created")+" with title: "+cur.getJSONObject("data").getString("title"));
                        }
                        else {
                            System.out.println(" Connects to previous user at "+cur.getJSONObject("data").getInt("created")+" with EMPTY title");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}