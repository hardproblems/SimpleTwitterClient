package com.codepath.simpletwitterclient.app.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.fragments.UserTimelineFragment;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.codepath.simpletwitterclient.app.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ViewProfileActivity extends FragmentActivity {
	public static final String USER_ID_EXTRA = "user_id";
	public static final String SCREENNAME_EXTRA = "screen_name";

	private FrameLayout flHeaderContainer;
	private ImageView ivProfile;
	private TextView tvName;
	private TextView tvDescription;
	private TextView tvTweetsCount;
	private TextView tvFollowingCount;
	private TextView tvFollowersCount;

	private String userIdStr;
	private String screenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);
		setupViews();
		populateHeader();
		addUserTimelineFragment();
	}

	private void addUserTimelineFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment fragmentDemo = UserTimelineFragment.newInstance(screenName);
		ft.replace(R.id.flTimelineContainer, fragmentDemo);
		ft.commit();
	}

	private void setupViews() {
		flHeaderContainer = (FrameLayout) findViewById(R.id.flHeaderContainer);
		ivProfile = (ImageView) findViewById(R.id.ivProfileImage);
		tvName = (TextView) findViewById(R.id.tvProfileName);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvTweetsCount = (TextView) findViewById(R.id.tvTweetsCount);
		tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);
		tvFollowersCount = (TextView) findViewById(R.id.tvFollowersCount);
	}

	private void populateHeader() {
		Intent data = getIntent();
		userIdStr = data.getStringExtra(USER_ID_EXTRA);
		if (userIdStr == null) {
			userIdStr = MyTwitterApp.getCurrentUser().idStr;
			populateProfile(MyTwitterApp.getCurrentUser());
		} else {
			screenName = data.getStringExtra(SCREENNAME_EXTRA);
			MyTwitterApp.getRestClient().getUser(userIdStr, screenName, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject jsonUser) {
					User user = Utils.fromJson(User.class, jsonUser);
					populateProfile(user);
				}

				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					Toast.makeText(getBaseContext(), "Oops...failed to load user!", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private void populateProfile(User user) {
		ImageLoader.getInstance().displayImage(user.profileImageUrl, ivProfile);
		tvName.setText(user.name);
		if (user.description != null) {
			tvDescription.setText(user.description);
		}
		tvTweetsCount.setText(String.valueOf(user.numTweets));
		tvFollowersCount.setText(String.valueOf(user.numFollowers));
		tvFollowingCount.setText(String.valueOf(user.numFollowing));
		if (user.profileBannerUrl != null) {
			ImageLoader.getInstance().loadImage(user.profileBannerUrl, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					flHeaderContainer.setBackgroundDrawable(new BitmapDrawable(flHeaderContainer.getResources(), loadedImage));
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
