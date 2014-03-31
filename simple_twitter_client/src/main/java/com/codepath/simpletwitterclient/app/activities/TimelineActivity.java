package com.codepath.simpletwitterclient.app.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.adapters.TweetsAdapter;
import com.codepath.simpletwitterclient.app.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private TweetsAdapter tweetsAdapter;
	private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
	    lvTweets = (ListView) findViewById(R.id.lvTweets);
	    tweetsAdapter = new TweetsAdapter(this, new ArrayList<Tweet>());
	    lvTweets.setAdapter(tweetsAdapter);
	    MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
		    @Override
		    public void onSuccess(JSONArray jsonTweets) {
			    Log.d("DEBUG", jsonTweets.toString());
			    List<Tweet> tweets = Tweet.fromJson(jsonTweets);
			    tweetsAdapter.addAll(tweets);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
