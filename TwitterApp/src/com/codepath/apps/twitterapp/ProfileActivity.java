package com.codepath.apps.twitterapp;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private String userId;
	private String screenName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		userId = getIntent().getStringExtra("userId");
		screenName = getIntent().getStringExtra("screenName");
		loadProfileInfo();
	}

	private void loadProfileInfo() {
		
		TwitterClientApp.getRestClient().getUserInfo(userId, screenName, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject json) {
				Log.d("DEBUG", "getUserInfo:"+json.toString());
				User u = User.fromJson(json);
				getActionBar().setTitle("@"+ screenName);
				populateProfileHeader(u);
			
			    UserTimelineFragment userTimelinefragment = (UserTimelineFragment) getSupportFragmentManager().findFragmentById(
						R.id.fragmentUserTimeline);
				userTimelinefragment.setUserId(userId);
			}
			
			@Override
			public void onFailure(Throwable arg0, String message) {
				Log.d("DEBUG", "Failed to fetch user info:"
						+ message);
			}
		});
		
	}
	

	private void populateProfileHeader(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
