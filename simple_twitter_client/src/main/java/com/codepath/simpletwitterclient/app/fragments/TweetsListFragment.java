package com.codepath.simpletwitterclient.app.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.codepath.simpletwitterclient.app.EndlessScrollListener;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.adapters.TweetsAdapter;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.codepath.simpletwitterclient.app.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
	protected ArrayList<Tweet> tweetsList;
	protected TweetsAdapter tweetsAdapter;
	protected ListView lvTweets;

	private PullToRefreshLayout mPullToRefreshLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		tweetsList = new ArrayList<Tweet>();
		tweetsAdapter = new TweetsAdapter(getActivity(), tweetsList);
		lvTweets = (ListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(tweetsAdapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener(3) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				long maxId = tweetsAdapter.getItem(totalItemsCount - 1).tweetId - 1;
				refreshTimeline(25, -1, maxId);
			}
		});
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// This is the View which is created by ListFragment
		ViewGroup viewGroup = (ViewGroup) view;

		// We need to create a PullToRefreshLayout manually
		mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

		// We can now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
				// We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
				.insertLayoutInto(viewGroup)
						// We need to mark the ListView and it's Empty View as pullable
						// This is because they are not dirent children of the ViewGroup
				.theseChildrenArePullable(lvTweets, lvTweets.getEmptyView())
						// We can now complete the setup as desired
				.listener(new OnRefreshListener() {
					@Override
					public void onRefreshStarted(View view) {
						long sinceId = -1;
						if (!tweetsList.isEmpty()) {
							sinceId = tweetsList.get(0).tweetId - 1; // -1 so we can reuse refreshTimeline with post new tweet
						}
						refreshTimeline(25, sinceId, -1);
					}
				})
				.setup(mPullToRefreshLayout);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refreshTimeline(25);
	}

	private void refreshTimeline(int count) {
		refreshTimeline(count, -1, -1);
	}

	private void refreshTimeline(int count, final long sinceId, long maxId) {
		if (!Utils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "Could not connect to network!", Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
			return;
		}
		fetchTweets(count, sinceId, maxId);
	}

	protected abstract void fetchTweets(int count, long sinceId, long maxId);

	protected AsyncHttpResponseHandler getHandler(final long sinceId) {
		return new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", jsonTweets.toString());
				List<Tweet> newTweets = Utils.fromJson(Tweet.class, jsonTweets);
				if (sinceId < 0) {
					// Infinite scroll
					tweetsAdapter.addAll(newTweets);
				} else {
					// Pull to refresh or post new tweet
					tweetsList.remove(0); // remove the topmost because newTweets should contain it
					tweetsList.addAll(0, newTweets);
					tweetsAdapter.notifyDataSetChanged();
				}
				mPullToRefreshLayout.setRefreshComplete();
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Toast.makeText(getActivity(), "Oops...failed to load tweets!", Toast.LENGTH_SHORT).show();
			}
		};
	}

	public void postToTop(Tweet topmostTweet) {
		long sinceId = tweetsAdapter.getItem(0).tweetId;
		tweetsList.add(0, topmostTweet);
		tweetsAdapter.notifyDataSetChanged();
		refreshTimeline(25, sinceId, -1);
	}
}
