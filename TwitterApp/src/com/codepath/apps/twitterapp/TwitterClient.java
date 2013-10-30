package com.codepath.apps.twitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

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
    public static final String REST_CONSUMER_KEY = "voLIYWyCd74M2i17q2cA";       // Change this
    public static final String REST_CONSUMER_SECRET = "NzqFLalFbvM3ZkNaMZ5ujxDMlBKYStRsfh2tFZh39Q"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://twitterapp"; // Change this (here and in manifest)
	private static final int COUNT = 20;
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getMyInfo(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
    	client.get(url, null, handler);
    }
    
    public void getUserInfo(String userId, String screenName, AsyncHttpResponseHandler handler) {
    	Log.d("DEBUG", "getUserInfo:: uid:"+userId+" screenName:"+screenName);
    	String url = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("screen_name", screenName);
    	client.get(url, params, handler);
    }
    
    public void getUserTimeline(String userId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
    	client.get(url, params, handler);
    }
    
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String url = getApiUrl("statuses/home_timeline.json");
        client.get(url, null, handler);
    }
    
    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
    		String url = getApiUrl("statuses/home_timeline.json");
            RequestParams params = new RequestParams();
            params.put("count", Integer.toString(COUNT));
            if(maxId > 0) 
            	params.put("max_id", Long.toString(maxId));
            client.get(url, params, handler);
    }
    
    public void getMentions(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
        if(maxId > 0) 
        	params.put("max_id", Long.toString(maxId));
        client.get(url, params, handler);
    }
    
    public void postTweet(String tweetStatus, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweetStatus);
    	client.post(url, params, handler);
    }

}