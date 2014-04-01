package com.codepath.simpletwitterclient.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.codepath.simpletwitterclient.app.EndlessScrollListener;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.adapters.TweetsAdapter;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.codepath.simpletwitterclient.app.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class TimelineActivity extends Activity {
	public static final int COMPOSE_REQUEST = 1;

	private PullToRefreshLayout mPullToRefreshLayout;
	private ArrayList<Tweet> tweetsList = new ArrayList<Tweet>();
	private TweetsAdapter tweetsAdapter;
	private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
	    setTitle(String.format("@%s", MyTwitterApp.getCurrentUser().screenName));

	    mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
	    // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(this)
            // Mark All Children as pullable
            .allChildrenArePullable()
            // Set a OnRefreshListener
            .listener(new OnRefreshListener() {
	            @Override
	            public void onRefreshStarted(View view) {
		            long sinceId = -1;
		            if (!tweetsList.isEmpty()) {
			            sinceId = tweetsList.get(0).tweetId - 1; // -1 so we can reuse refreshTimeline with post new tweet
		            }
		            refreshTimeline(200, sinceId, -1);
	            }
            })
            // Finally commit the setup to our PullToRefreshLayout
            .setup(mPullToRefreshLayout);

	    lvTweets = (ListView) findViewById(R.id.lvTweets);
	    tweetsAdapter = new TweetsAdapter(this, tweetsList);
	    lvTweets.setAdapter(tweetsAdapter);
	    lvTweets.setOnScrollListener(new EndlessScrollListener(3) {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
			    long maxId = tweetsAdapter.getItem(totalItemsCount-1).tweetId - 1;
				refreshTimeline(25, -1, maxId);
		    }
	    });
		refreshTimeline(25);
    }

	private void refreshTimeline(int count) {
		refreshTimeline(count, -1, -1);
	}

	private void refreshTimeline(int count, final long sinceId, long maxId) {
		if (!Utils.isNetworkAvailable(getBaseContext())) {
			Toast.makeText(this, "Could not connect to network!", Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
			return;
		}
		MyTwitterApp.getRestClient().getHomeTimeline(count, sinceId, maxId, new JsonHttpResponseHandler() {
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
			    Toast.makeText(getBaseContext(), "Oops...failed to load tweets from time line!", Toast.LENGTH_SHORT).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_compose) {
	        startActivityForResult(new Intent(getBaseContext(), ComposeActivity.class), COMPOSE_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST) {
			Tweet myLatestTweet = Utils.fromJson(Tweet.class, data.getStringExtra("tweet"));
			long sinceId = tweetsAdapter.getItem(0).tweetId;
			tweetsList.add(0, myLatestTweet);
			tweetsAdapter.notifyDataSetChanged();
			refreshTimeline(25, sinceId, -1);
		}
	}
}
