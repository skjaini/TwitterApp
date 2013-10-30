package com.codepath.apps.twitterapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.TwitterClientApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void fetchUserTimeline() {
		TwitterClientApp.getRestClient().getUserTimeline(userId,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						Log.d("DEBUG", "jsonTweets:" + jsonTweets.toString());

						getAdapter().addAll(Tweet.fromJson(jsonTweets));
					}
				});
	}

	public void setUserId(String id) {
		Log.d("DEBUG", "UserTimelineFragment userid:" + id);
		userId = id;
		fetchUserTimeline();
	}
}
