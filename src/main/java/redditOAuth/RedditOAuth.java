package redditOAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;

// Reddit's OAUTH docs are here:
// https://github.com/reddit/reddit/wiki/OAuth2
// http://www.reddit.com/r/redditdev/comments/218wd7/oauth_20_you_asked_i_listened_updated_and_more/


// Docs for Google HTTP lib, and GAE urlfetch:
// https://code.google.com/p/google-http-java-client/
// https://developers.google.com/appengine/docs/java/urlfetch/

// Apache License
// copyright 2014 /u/redlist-admin

// You will need to comment out or replace my custom Log statements.

public class RedditOAuth {

     public static final String OAUTH_API_DOMAIN = "https://oauth.reddit.com";

    // Step 1. Send user to auth URL
    public static final String OAUTH_AUTH_URL = "https://ssl.reddit.com/api/v1/authorize";
    // https://ssl.reddit.com/api/v1/authorize?client_id=CLIENT_ID&response_type=TYPE&state=RANDOM_STRING&redirect_uri=URI&duration=DURATION&scope=SCOPE_STRING

    // Step 2. Reddit sends user to REDIRECT_URI
    private static final String REDIRECT_URI = "http://127.0.0.1:8080/callback";

    // Step 3. Get token
    public static final String OAUTH_TOKEN_URL = "https://ssl.reddit.com/api/v1/access_token";

    // I think it is easier to create 2 reddit apps (one with 127.0.0.1 redirect URI)
    public static final String MY_APP_ID = "7Wg6jZ-0E8QOQQ";
    public static final String MY_APP_SECRET = "e2D1s-mAIhUaZzIHt48WXSKPgJY";

    public static final String SCOPE_ID = "identity,read,report";

    public static boolean permanentAccess = true;

    public static String getUserAuthUrl(String state) {
        String duration = permanentAccess ? "permanent" : "temporary";
        String url = OAUTH_AUTH_URL + "?client_id=" + MY_APP_ID + "&response_type=code&state=" + state
                + "&redirect_uri=" + REDIRECT_URI + "&duration=" + duration + "&scope=" + SCOPE_ID;

        // scopes: modposts, identity, edit, flair, history, modconfig, modflair, modlog, modposts, modwiki,
        // mysubreddits, privatemessages, read, report, save, submit, subscribe, vote, wikiedit, wikiread, etc.
        return url;
    }

    private static Logger logger = Logger.getGlobal();

    // The Google Java HTTP library has a 'pluggable' architecture - the following line selects the URLFetch transport,
    // which is the native HTTP api for GAE.  'NetHttpTransport()' would be a more generic alternative.
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public static JSONObject getToken(String code) throws IOException {
        logger.log(Level.INFO, "getToken for code=" + code);
        GenericUrl url = new GenericUrl(OAUTH_TOKEN_URL);
        Map<String, String> params = new HashMap<String, String>(3);
        params.put("grant_type", "client_credentials");
        //params.put("code", code);
        //params.put("redirect_uri", REDIRECT_URI);
        HttpContent hc = new UrlEncodedContent(params);

        HttpRequestFactory requestFactory = HTTP_TRANSPORT
                .createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        // request.setParser( new JsonObjectParser(JSON_FACTORY) );
                        request.getHeaders().setBasicAuthentication(MY_APP_ID, MY_APP_SECRET);
                    }
                });

        HttpRequest request = requestFactory.buildPostRequest(url, hc);
        HttpResponse response = request.execute();

        JSONObject jo = null;
        try {
            if (response.isSuccessStatusCode()) {

                String json = response.parseAsString();
                logger.log(Level.INFO, "Success with " + json);

                // Parse with org.json
                JSONTokener tokener = null;
                tokener = new JSONTokener( json );
                jo = new JSONObject(tokener);

                // Sample response:
                // {"access_token": "cdkVPfKww5R0D1v-MJAD89Y19QM",
                // "token_type": "bearer",
                // "expires_in": 3600,
                // "refresh_token": "vzZ0PP0A4k-twzSuVyvRN7uH2JY",
                // "scope": "identity"}
            } else
                logger.log(Level.WARNING , "Request failed with " + response.getStatusCode());
        } finally {
            response.disconnect();
        }

        return jo;
    }

    // http://www.reddit.com/dev/api
    // https://oauth.reddit.com/api/v1/me
    public static final String ENDPOINT_ID = OAUTH_API_DOMAIN + "/api/v1/me";


    // A generic get fn to build the rest of my API calls around
    public static JSONObject getObject( final String surl, final String token ) throws IOException {
        logger.log(Level.INFO, "get for URL=" + surl);

        GenericUrl url = new GenericUrl( surl );

        HttpRequestFactory requestFactory = HTTP_TRANSPORT
                .createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        // request.setParser( new JsonObjectParser(JSON_FACTORY) );
                        request.getHeaders().setAuthorization( "bearer " + token );
                    }
                });

        HttpRequest request = requestFactory.buildGetRequest( url );
        HttpResponse response = request.execute();

        JSONObject jo = null;

        // Note the recommended use of finally { disconnect() }
        try {
            if (response.isSuccessStatusCode()) {

                String json = response.parseAsString();
                logger.log(Level.INFO, "Success with " + json);

                // Parse with org.json
                JSONTokener tokener = null;
                tokener = new JSONTokener( json );
                jo = new JSONObject(tokener);

                // Or Parse directly into Java objects using Jackson

            } else
                logger.log(Level.WARNING, "Request failed with " + response.getStatusCode());
        } finally {
            response.disconnect();
        }

        return jo;
    }

    public static JSONArray getArray( final String surl, final String token ) throws IOException {
        logger.log(Level.INFO, "get for URL=" + surl);

        GenericUrl url = new GenericUrl( surl );

        HttpRequestFactory requestFactory = HTTP_TRANSPORT
                .createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        // request.setParser( new JsonObjectParser(JSON_FACTORY) );
                        request.getHeaders().setAuthorization( "bearer " + token );
                    }
                });

        HttpRequest request = requestFactory.buildGetRequest( url );
        HttpResponse response = request.execute();

        JSONArray ja = null;

        // Note the recommended use of finally { disconnect() }
        try {
            if (response.isSuccessStatusCode()) {

                String json = response.parseAsString();
                logger.log(Level.INFO, "Success with " + json);

                // Parse with org.json
                JSONTokener tokener = null;
                tokener = new JSONTokener( json );
                ja = new JSONArray(tokener);
            } else
                logger.log(Level.WARNING, "Request failed with " + response.getStatusCode());
        } finally {
            response.disconnect();
        }
        return ja;
    }

}