package com.codepath.simpletwitterclient.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
	@JsonProperty("id_str")
	public String idStr;

	public String name;
	public String description;

	@JsonProperty("screen_name")
	public String screenName;
	@JsonProperty("profile_image_url")
	public String profileImageUrl;
	@JsonProperty("followers_count")
	public int numFollowers;
	@JsonProperty("friends_count")
	public int numFollowing;
	@JsonProperty("statuses_count")
	public int numTweets;
}
