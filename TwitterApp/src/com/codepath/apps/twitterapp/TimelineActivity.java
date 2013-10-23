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
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private TweetsAdapter adapter;
	private User user;
	private long maxId = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		getActionBar().setDisplayUseLogoEnabled(true);

		// Fetch user account
		getUserAccount();

		// set the list view
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		adapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(adapter);

		// Fetch home timeline
		loadDataFromApi();

		// attach scroll listener
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				maxId = tweets.get(tweets.size() - 1).getId() - 1;
				loadDataFromApi();
			}
		});

	}

	public void getUserAccount() {
		TwitterClientApp.getRestClient().getAccountCredentials(
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonUser) {
						try {
							setTitle("@" + jsonUser.getString("screen_name"));
							user = User.fromJson(jsonUser);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String message) {
						Log.d("DEBUG", "Failed to fetch user settings:"
								+ message);
					}
				});
	}

	public void loadDataFromApi() {

		TwitterClientApp.getRestClient().getHomeTimeline(maxId,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						Log.d("DEBUG", "jsonTweets:" + jsonTweets.toString());

						tweets = Tweet.fromJson(jsonTweets);
						adapter.addAll(tweets);
						adapter.notifyDataSetChanged();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: For Refresh action
		if (item.getItemId() == R.id.abRefresh) {
			Toast.makeText(this, "loading..", Toast.LENGTH_SHORT)
					.show();
			
			adapter.clear();
			loadDataFromApi();
		}

		// For Post action
		if (item.getItemId() == R.id.abPost) {
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
			// Toast.makeText(this, data.getExtras().getString("name"),
			// Toast.LENGTH_SHORT).show();

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
