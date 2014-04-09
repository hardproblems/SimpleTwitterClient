package com.codepath.simpletwitterclient.app.fragments;

import com.codepath.simpletwitterclient.app.MyTwitterApp;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	protected void fetchTweets(int count, final long sinceId, long maxId) {
		MyTwitterApp.getRestClient().getHomeTimeline(count, sinceId, maxId, getHandler(count, sinceId, maxId));
	}
}
