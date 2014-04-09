package com.codepath.simpletwitterclient.app;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "BQ7GaOf3DwR3zCnp5HmFgg";       // Change this
    public static final String REST_CONSUMER_SECRET = "ChXJjAuP9KmG7UvNAr5ihceQsFkG2fLQxp7CPr84"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)

	public static final String HOME_TIMELINE_PATH = "statuses/home_timeline.json";
	public static final String MENTIONS_TIMELINE_PATH = "statuses/mentions_timeline.json";
	public static final String VERIFY_CREDENTIALS_PATH = "account/verify_credentials.json";
	public static final String POST_UPDATE_PATH = "statuses/update.json";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

	 /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

	public void getHomeTimeline(int count, long sinceId, long maxId, AsyncHttpResponseHandler handler) {
		getTimeline(HOME_TIMELINE_PATH, count, sinceId, maxId, handler);
	}

	public void getMentionsTimeline(int count, long sinceId, long maxId, AsyncHttpResponseHandler handler) {
		getTimeline(MENTIONS_TIMELINE_PATH, count, sinceId, maxId, handler);
	}

	private void getTimeline(String apiPath, int count, long sinceId, long maxId, AsyncHttpResponseHandler handler) {
		String url = getApiUrl(apiPath);
		RequestParams params = new RequestParams();
		params.put("count", String.valueOf(count));
		if (sinceId > 0) {
			params.put("since_id", String.valueOf(sinceId));
		}
		if (maxId > 0) {
			params.put("max_id", String.valueOf(maxId));
		}
		client.get(url, params, handler);
	}

	public void verifyCredentials(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(VERIFY_CREDENTIALS_PATH);
		client.get(apiUrl, handler);
	}

	public void postStatusUpdate(String status, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(POST_UPDATE_PATH);
		RequestParams params = new RequestParams();
		params.put("status", status);
		client.post(apiUrl, params, handler);
	}
}