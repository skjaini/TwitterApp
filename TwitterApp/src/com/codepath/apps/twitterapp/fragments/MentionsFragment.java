package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import com.codepath.apps.twitterapp.TwitterClientApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;

public class MentionsFragment extends TweetsListFragment {
	private long maxId = 0;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterClientApp.getRestClient().getMentions(maxId,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						Log.d("DEBUG", "jsonTweets:" + jsonTweets.toString());

						tweets = Tweet.fromJson(jsonTweets);
						getAdapter().addAll(tweets);
						getAdapter().notifyDataSetChanged();
					}
				});
	}
}
