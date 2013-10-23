package com.codepath.apps.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private static final int REQUEST_CODE = 0;
	private ListView lvTweets;
	private TweetsAdapter adapter;
	private User user;
	private long maxId = 0;
    private long sinceId = Long.MAX_VALUE;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		getActionBar().setDisplayUseLogoEnabled(true);
		
		// Fetch user settings
		TwitterClientApp.getRestClient().getAccountCredentials(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				try {
					setTitle("@"+jsonUser.getString("screen_name"));
					user = User.fromJson(jsonUser);
					Log.d("DEBUG", "user1:"+user.getJSONString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String message) {
			//	Toast.makeText(this, "Failed to fetch user settings:" + message, Toast.LENGTH_LONG).show();
				Log.d("DEBUG", "Failed to fetch user settings:" + message);
			}
		});
		
		// Fetch home timeline
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				maxId = tweets.get(tweets.size()-1).getId() - 1;
				sinceId = tweets.get(0).getId();
				
				lvTweets = (ListView) findViewById(R.id.lvTweets);
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
			}
		});
		
		/*
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			 @Override
             public void onLoadMore(int page, int totalItemsCount) {
                     TwitterClientApp.getRestClient().getHomeTimeline(handler, maxId);
             }
		});
		*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// For Refresh action
		if (item.getItemId() == R.id.abRefresh) {
			Toast.makeText(this, "Item: " + item.toString(), Toast.LENGTH_SHORT).show();
		}
		
		// For Post action
		if (item.getItemId() == R.id.abPost ) {
			Intent postIntent = new Intent();
			postIntent.putExtra("screenName", user.getScreenName());
			postIntent.putExtra("profileImageUrl", user.getProfileImageUrl());
			
			postIntent.setClass(getApplicationContext(), PostActivity.class);
			startActivityForResult(postIntent, 1);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	   //  Toast.makeText(this, data.getExtras().getString("name"), Toast.LENGTH_SHORT).show();
	     
	     Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");
	     adapter.add(tweet);
	     
	     // notify adapter refresh
	     adapter.notifyDataSetChanged();
	  }
	  
	  if (resultCode == RESULT_CANCELED) {
		  Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
	  }
	} 
	
}
