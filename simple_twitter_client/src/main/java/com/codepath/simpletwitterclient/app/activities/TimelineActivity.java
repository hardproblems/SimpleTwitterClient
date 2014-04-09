package com.codepath.simpletwitterclient.app.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.codepath.simpletwitterclient.app.FragmentTabListener;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.codepath.simpletwitterclient.app.R;
import com.codepath.simpletwitterclient.app.fragments.HomeTimelineFragment;
import com.codepath.simpletwitterclient.app.fragments.MentionsTimelineFragment;
import com.codepath.simpletwitterclient.app.fragments.TweetsListFragment;
import com.codepath.simpletwitterclient.app.helpers.Utils;
import com.codepath.simpletwitterclient.app.models.Tweet;

public class TimelineActivity extends FragmentActivity {
	public static final String HOME_TAB = "HomeTimeline";
	public static final String MENTIONS_TAB = "MentionsTimeline";

	public static final int COMPOSE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
	    setTitle(String.format("@%s", MyTwitterApp.getCurrentUser().screenName));
	    setupNavigationTabs();
    }

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		ActionBar.Tab homeTimelineTab = actionBar.newTab()
				.setText(getText(R.string.home_tab_title)).setIcon(R.drawable.ic_home)
				.setTag(HOME_TAB)
				.setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this,
                        HOME_TAB, HomeTimelineFragment.class));

		actionBar.addTab(homeTimelineTab);
		actionBar.selectTab(homeTimelineTab);

		ActionBar.Tab mentionsTab = actionBar.newTab()
				.setText(getText(R.string.mentions_tab_title)).setIcon(R.drawable.ic_at_sign)
				.setTag(MENTIONS_TAB)
				.setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, MENTIONS_TAB, MentionsTimelineFragment.class));
		actionBar.addTab(mentionsTab);
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
        } else if (id == R.id.action_view_profile) {
	        // Start profile activity
	        startActivity(new Intent(getBaseContext(), ViewProfileActivity.class));
	        return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST) {
			Tweet myLatestTweet = Utils.fromJson(Tweet.class, data.getStringExtra("tweet"));
			String selectedTag = getActionBar().getSelectedTab().getTag().toString();
			((TweetsListFragment) getSupportFragmentManager().findFragmentByTag(selectedTag)).postToTop(myLatestTweet);
		}
	}
}
