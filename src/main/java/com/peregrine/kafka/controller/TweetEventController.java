package com.peregrine.kafka.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peregrine.kafka.TweetEvent;
import com.peregrine.kafka.producer.TweetEventProducer;

@RestController
public class TweetEventController {
	
	@Autowired
	TweetEventProducer tweetEventProducer;
	
	@Value("${isTestEnabled}")
	private Boolean isTestEnabled;

	@PostMapping("/v1/tweetEvent")
	public ResponseEntity<TweetEvent> postTweetEvent(@RequestBody TweetEvent tweetEvent) throws JsonProcessingException {
		if(isTestEnabled) {
			return ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent);
		}
		tweetEvent.getTweet().setCreateDttm(new Date());
		//invoke kafka producer
		tweetEventProducer.sendTweetEvent(tweetEvent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent);
	}
	
}
