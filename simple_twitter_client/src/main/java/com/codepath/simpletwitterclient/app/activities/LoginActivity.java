package com.codepath.simpletwitterclient.app.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.codepath.oauth.OAuthLoginActivity;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.TwitterClient;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.codepath.simpletwitterclient.app.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
	    MyTwitterApp.getRestClient().verifyCredentials(new JsonHttpResponseHandler() {
		    @Override
		    public void onSuccess(int arg0, JSONObject jsonObject) {
			    MyTwitterApp.setCurrentUser(Utils.fromJson(User.class, jsonObject));
			    Intent i = new Intent(getBaseContext(), TimelineActivity.class);
			    startActivity(i);
		    }

		    @Override
		    public void onFailure(Throwable e, JSONObject errorResponse) {
			    Toast.makeText(getBaseContext(), "Could not verify the current user!", Toast.LENGTH_SHORT).show();
			    Log.d("DEBUG", "Failed to get current user");
		    }
	    });
    }
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
	    e.printStackTrace();
    }
    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        if (!Utils.isNetworkAvailable(this)) {
		    Toast.makeText(getBaseContext(), "Could not connect to network!", Toast.LENGTH_SHORT).show();
	    }
	    getClient().connect();
    }

}
