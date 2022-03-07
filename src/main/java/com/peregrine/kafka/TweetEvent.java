package com.peregrine.kafka;

import com.peregrine.kafka.bean.Tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TweetEvent {
	
	private Integer tweetEventId;
	private TweetEventType tweetEventType;
	private String currentUser;
	private Tweet tweet;
}
