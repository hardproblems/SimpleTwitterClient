package com.codepath.simpletwitterclient.app;

import android.content.Context;
import com.codepath.simpletwitterclient.app.models.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     TwitterClient client = MyTwitterApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class MyTwitterApp extends com.activeandroid.app.Application {
	private static Context context;
	private static User currentUser;

	@Override
	public void onCreate() {
		super.onCreate();
		MyTwitterApp.context = this;

		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
				cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, MyTwitterApp.context);
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		MyTwitterApp.currentUser = currentUser;
	}
}