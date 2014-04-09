package com.codepath.simpletwitterclient.app.helpers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.Log;
import com.codepath.simpletwitterclient.app.models.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final String TWEET_TIME_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
	private static final SimpleDateFormat sf = new SimpleDateFormat(TWEET_TIME_FORMAT, Locale.ENGLISH);

	static {
		sf.setLenient(true);
	}

	private Utils() {
		// Don't new me
	}

	public static <T> T fromJson(Class<T> type, String jsonObjString) {
		assert (type != null);
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(jsonObjString, type);
		} catch (IOException e) {
			Log.e("ERROR", String.format("Could not deserialize to type %s", type));
		}
		return result;
	}

	public static <T> T fromJson(Class<T> type, JSONObject jsonObject) {
		return fromJson(type, jsonObject.toString());
	}

	public static <E> ArrayList<E> fromJson(Class<E> type, JSONArray jsonArray) {
		ArrayList<E> results = new ArrayList<E>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				E item = fromJson(type, jsonArray.getJSONObject(i).toString());
				if (item != null) {
					results.add(item);
				}
			} catch (JSONException e) {
				Log.w("WARN", String.format("Cannot get jsonObject at index %s from jsonArray", i), e);
				continue;
			}
		}
		return results;
	}

	private static Date getTweetDate(Tweet tweet) {
		Date result = null;
		try {
			result = sf.parse(tweet.createdAt);
		} catch (ParseException e) {
			Log.d("DEBUG", "Could not parse createdDate");
		}
		return result;
	}

	public static String getRelativeDate(Tweet tweet) {
		Date tweetDate = getTweetDate(tweet);
		if (tweetDate == null) {
			return "";
		}
		return DateUtils.getRelativeTimeSpanString(tweetDate.getTime(), System.currentTimeMillis(),
				DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}
}
