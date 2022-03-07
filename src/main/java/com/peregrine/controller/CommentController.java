package com.peregrine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peregrine.constant.MessageConstants;
import com.peregrine.service.CommentServiceManager;
import com.peregrine.service.ServiceHelper;
import com.peregrine.tweet.bean.CommentRequest;
import com.peregrine.tweet.bean.CommentResponse;

@RestController
public class CommentController {
	
	@Autowired
	CommentServiceManager commentServiceManager;
	
	@Autowired
	ServiceHelper serviceHelper;
	
	@PostMapping("/api/v1.0/tweets/postComment")
	public ResponseEntity<CommentResponse> postComment(@RequestBody CommentRequest reqBean, HttpServletRequest request) {
		ResponseEntity<CommentResponse> response = null;
		CommentResponse commentResponse = new CommentResponse();
		try {
			response = commentServiceManager.postNewComment(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@PostMapping("/api/v1.0/tweets/fetchComments")
	public ResponseEntity<CommentResponse> getAllCommentsByTweetId(@RequestBody CommentRequest reqBean) {
		ResponseEntity<CommentResponse> response = null;
		CommentResponse commentResponse = new CommentResponse();
		try {
			response = commentServiceManager.retrieveAllCommentsByTweetId(reqBean);
		} catch (Exception e) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@PutMapping("/api/v1.0/tweets/updateComment")
	public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest reqBean, HttpServletRequest request) {
		ResponseEntity<CommentResponse> response = null;
		CommentResponse commentResponse = new CommentResponse();
		try {
			response = commentServiceManager.postUpdateComment(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@PutMapping("/api/v1.0/tweets/likeComment")
	public ResponseEntity<CommentResponse> likeComment(@RequestBody CommentRequest reqBean, HttpServletRequest request) {
		ResponseEntity<CommentResponse> response = null;
		CommentResponse commentResponse = new CommentResponse();
		try {
			response = commentServiceManager.postLikeComment(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
	
	@DeleteMapping("/api/v1.0/tweets/deleteComment")
	public ResponseEntity<CommentResponse> deleteComment(@RequestBody CommentRequest reqBean, HttpServletRequest request) {
		ResponseEntity<CommentResponse> response = null;
		CommentResponse commentResponse = new CommentResponse();
		try {
			response = commentServiceManager.postDeleteComment(reqBean, request);
		} catch (Exception e) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		if(response == null) {
			response = new ResponseEntity<>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			response.getBody().setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
		}
		return response;
	}
}
