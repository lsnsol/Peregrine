package com.peregrine.tweet.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.kafka.bean.Tweet;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
@Component
public class TweetResponse {

	private String username;
	private String tweetId;
	private	List<SvcMessage> messages;
	private	List<Tweet> tweets;
	private Tweet tweet;
}
