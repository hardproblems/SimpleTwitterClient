package com.codepath.simpletwitterclient.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ComposeActivity extends Activity {
	EditText etTweet;
	TextView tvCharsLeft;
	Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
	    setupViews();
    }

	public void setupViews() {
		etTweet = (EditText) findViewById(R.id.etTweet);
		tvCharsLeft = (TextView) findViewById(R.id.tvCharsLeft);
		btnTweet = (Button) findViewById(R.id.btnTweet);
		etTweet.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Nothing to do
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Nothing to do
			}

			@Override
			public void afterTextChanged(Editable s) {
				int charsLeft = 140 - etTweet.getText().length();
				tvCharsLeft.setText(String.valueOf(charsLeft));
				if (charsLeft > 15) {
					tvCharsLeft.setTextColor(Color.GRAY);
				} else if (charsLeft > 5) {
					tvCharsLeft.setTextColor(Color.BLACK);
				} else {
					tvCharsLeft.setTextColor(Color.RED);
				}
				btnTweet.setEnabled(charsLeft > 0);
			}
		});
	}

	public void cancelTweet(View v) {
		finish();
	}

	public void sendTweet(View v) {
		MyTwitterApp.getRestClient().postStatusUpdate(etTweet.getText().toString(), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String content) {
				Toast.makeText(getBaseContext(), "Your tweet was posted successfully!", Toast.LENGTH_SHORT).show();
				Log.d("DEBUG", "Post response content: "+content);
				Intent data = new Intent();
				data.putExtra("tweet", content);
				setResult(RESULT_OK, data);
				finish();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				if (Utils.isNetworkAvailable(getBaseContext())) {
					Toast.makeText(getBaseContext(), "Failed to send your tweet. Please try again later!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), "Could not connect to network!", Toast.LENGTH_SHORT).show();
				}
				Log.e("ERROR", "Sending tweet failed!");
			}
		});
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       switch (item.getItemId()) {
	       case android.R.id.home:
		       NavUtils.navigateUpFromSameTask(this);
		       return true;

        }
	    return super.onOptionsItemSelected(item);
    }

}
