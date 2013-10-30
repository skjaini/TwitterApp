package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.codepath.apps.twitterapp.ProfileActivity;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TweetsAdapter;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;

public class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		ListView lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		
		if (getActivity().getClass() != ProfileActivity.class) {
            lvTweets.setItemsCanFocus(true);

            lvTweets.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Tweet tweet = adapter.getItem(position);
                            User user = tweet.getUser();
                            Intent i = new Intent(getActivity(), ProfileActivity.class);
                            i.putExtra("userId", String.valueOf(user.getId()));
                            i.putExtra("screenName", user.getScreenName());
                            startActivity(i);
                    }
            });
    }
	}

	public TweetsAdapter getAdapter() {
		return adapter;
	}
}
