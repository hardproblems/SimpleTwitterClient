package com.codepath.simpletwitterclient.app;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.codepath.simpletwitterclient.app.models.Tweet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
	    MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
		    @Override
		    public void onSuccess(JSONArray jsonTweets) {
			    Log.d("DEBUG", jsonTweets.toString());
			    try {
				    List<Tweet> tweets = MyTwitterApp.OBJECT_MAPPER.readValue(jsonTweets.toString(), new TypeReference<List<Tweet>>(){});
				    Log.d("DEBUG", "Read tweets successfully!");
			    } catch (IOException e) {
				    Log.e("ERROR", "Read tweets fail!");
			    }
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
