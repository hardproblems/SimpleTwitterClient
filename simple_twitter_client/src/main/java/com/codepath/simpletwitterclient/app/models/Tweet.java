package com.codepath.simpletwitterclient.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Tweet {
	@JsonProperty("id_str")
	public String idStr;
	public User user;
	public String text;
	@JsonProperty("created_at")
	public String createdAt;
}
