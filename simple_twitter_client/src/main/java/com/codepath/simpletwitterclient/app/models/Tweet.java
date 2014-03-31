package com.codepath.simpletwitterclient.app.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.util.Log;
import com.codepath.simpletwitterclient.app.MyTwitterApp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Tweet {
	@JsonProperty("id_str")
	public String idStr;
	public User user;
	public String text;
	@JsonProperty("created_at")
	public String createdAt;

	public static List<Tweet> fromJson(JSONArray jsonTweets) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			tweets = MyTwitterApp.OBJECT_MAPPER.readValue(jsonTweets.toString(), new TypeReference<List<Tweet>>(){});
			Log.d("DEBUG", "Read tweets successfully!");
		} catch (IOException e) {
			Log.e("ERROR", "Read tweets fail!");
		}
		return tweets;
	}
}
