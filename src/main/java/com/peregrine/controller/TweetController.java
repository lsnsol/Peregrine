package com.peregrine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peregrine.constant.MessageConstants;
import com.peregrine.service.ServiceHelper;
import com.peregrine.service.TweetServiceManager;
import com.peregrine.tweet.bean.TweetRequest;
import com.peregrine.tweet.bean.TweetResponse;

@RestController
public class TweetController {
	
	@Autowired
	TweetServiceManager tweetServiceManager;
	
	@Autowired
	ServiceHelper serviceHelper;
	
	@PostMapping("/api/v1.0/tweets/postTweet")
	public ResponseEntity<TweetResponse> postTweet(@RequestBody TweetRequest reqBean, HttpServletRequest request) {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = new TweetResponse();
		try {
			response = tweetServiceManager.postNewTweet(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@PostMapping("/api/v1.0/tweets/fetchTweets/username")
	public ResponseEntity<TweetResponse> getAllTweetsByUser(@RequestBody TweetRequest reqBean) {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = new TweetResponse();
		try {
			response = tweetServiceManager.retrieveAllTweetsByUser(reqBean);
		} catch (Exception e) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@GetMapping("/api/v1.0/tweets/fetchTweets")
	public ResponseEntity<TweetResponse> getAllTweets() {
		ResponseEntity<TweetResponse> response = null;
		try {
			response = tweetServiceManager.retrieveAllTweets();
		} catch (Exception e) {
			response = new ResponseEntity<TweetResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<TweetResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@PutMapping("/api/v1.0/tweets/updateTweet")
	public ResponseEntity<TweetResponse> updateTweet(@RequestBody TweetRequest reqBean, HttpServletRequest request) {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = new TweetResponse();
		try {
			response = tweetServiceManager.postUpdateTweet(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@PutMapping("/api/v1.0/tweets/likeTweet")
	public ResponseEntity<TweetResponse> likeTweet(@RequestBody TweetRequest reqBean, HttpServletRequest request) {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = new TweetResponse();
		try {
			response = tweetServiceManager.postLikeTweet(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@DeleteMapping("/api/v1.0/tweets/deleteTweet")
	public ResponseEntity<TweetResponse> deleteTweet(@RequestBody TweetRequest reqBean, HttpServletRequest request) {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = new TweetResponse();
		try {
			response = tweetServiceManager.postDeleteTweet(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
}
