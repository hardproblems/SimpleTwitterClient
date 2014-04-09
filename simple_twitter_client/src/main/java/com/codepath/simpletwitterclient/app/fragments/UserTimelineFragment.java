package com.codepath.simpletwitterclient.app.fragments;

import android.os.Bundle;
import com.codepath.simpletwitterclient.app.MyTwitterApp;

public class UserTimelineFragment extends TweetsListFragment {

	public static UserTimelineFragment newInstance(String screenName) {
		UserTimelineFragment fragment = new UserTimelineFragment();
		Bundle args = new Bundle();
		args.putString("screen_name", screenName);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected void fetchTweets(int count, long sinceId, long maxId) {
		String screenName = getArguments().getString("screen_name");
		MyTwitterApp.getRestClient().getUserTimeline(screenName, count, sinceId, maxId, getHandler(sinceId));
	}
}
