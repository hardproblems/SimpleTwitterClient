package com.codepath.simpletwitterclient.app.fragments;

import com.codepath.simpletwitterclient.app.MyTwitterApp;

public class MentionsTimelineFragment extends TweetsListFragment {
	@Override
	protected void fetchTweets(int count, long sinceId, long maxId) {
		MyTwitterApp.getRestClient().getMentionsTimeline(count, sinceId, maxId, getHandler(sinceId));
	}
}
