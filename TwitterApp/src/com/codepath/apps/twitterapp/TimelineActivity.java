package com.codepath.apps.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.fragments.MentionsFragment;
import com.codepath.apps.twitterapp.fragments.TweetsListFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {
	private static final int REQUEST_CODE = 0;
	private ListView lvTweets;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private TweetsAdapter adapter;
	private User user;
	private long maxId = 0;
	TweetsListFragment fragmentTweets;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		getActionBar().setDisplayUseLogoEnabled(true);

		// Fetch user account
		getUserAccount();
		
		setupNavigationTabs();

		//fragmentTweets = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
		// Fetch home timeline
		//loadDataFromApi();
	/*	
		// attach scroll listener
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				maxId = tweets.get(tweets.size() - 1).getId() - 1;
				loadDataFromApi();
			}
		});
*/
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment").setIcon(R.drawable.ic_action_home).setTabListener( this);
		
		Tab tabMentions = actionBar.newTab().setText("Mentions").setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_action_mentions).setTabListener(this);

		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
	}

	public void getUserAccount() {
		TwitterClientApp.getRestClient().getMyInfo(
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
/*
	public void loadDataFromApi() {

		TwitterClientApp.getRestClient().getHomeTimeline(maxId,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						Log.d("DEBUG", "jsonTweets:" + jsonTweets.toString());

						tweets = Tweet.fromJson(jsonTweets);
						fragmentTweets.getAdapter().addAll(tweets);
						adapter.notifyDataSetChanged();
					}
				});
	}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		if (item.getItemId() == R.id.abRefresh) {
			Toast.makeText(this, "loading..", Toast.LENGTH_SHORT)
					.show();
			
			// adapter.clear();
			// loadDataFromApi();
		}
		*/

		// For Post action
		if (item.getItemId() == R.id.abPost) {
			Intent postIntent = new Intent();
			postIntent.putExtra("screenName", user.getScreenName());
			postIntent.putExtra("profileImageUrl", user.getProfileImageUrl());

			postIntent.setClass(getApplicationContext(), PostActivity.class);
			startActivityForResult(postIntent, 1);
		}
		
		// for profile action
		if(item.getItemId() == R.id.abProfile) {
			Intent profileIntent = new Intent(this, ProfileActivity.class);
			profileIntent.putExtra("userId", String.valueOf(user.getId()));
			profileIntent.putExtra("screenName", user.getScreenName());
			
			startActivityForResult(profileIntent, 1);
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

			Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");
			adapter.add(tweet);

			// notify adapter refresh
			adapter.notifyDataSetChanged();
		}

		/*
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		}
		*/
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		
		if(tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
