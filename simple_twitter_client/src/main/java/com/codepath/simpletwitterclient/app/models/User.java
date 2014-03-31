package com.codepath.simpletwitterclient.app.models;

import org.json.JSONException;
import org.json.JSONObject;

class User {
	private String idStr;
	private String name;
	private String screenName;
	private String profileImageUrl;

	public String getIdStr() {
		return idStr;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public User fromJson(JSONObject jsonObject) {
		User user = new User();
		try {
			user.screenName = jsonObject.getString("screen_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
}
