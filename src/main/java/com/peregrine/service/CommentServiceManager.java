package com.peregrine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.constant.MessageConstants;
import com.peregrine.kafka.CommentEvent;
import com.peregrine.kafka.CommentEventType;
import com.peregrine.kafka.bean.Comment;
import com.peregrine.kafka.controller.CommentEventController;
import com.peregrine.repository.CommentRepository;
import com.peregrine.repository.TweetRepository;
import com.peregrine.security.jwt.JwtTokenUtil;
import com.peregrine.tweet.bean.CommentBean;
import com.peregrine.tweet.bean.CommentRequest;
import com.peregrine.tweet.bean.CommentResponse;
import com.peregrine.utils.LoggingUtils;

@Service
public class CommentServiceManager {

	@Autowired
	ServiceHelper serviceHelper;

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	CommentEventController commentEventController;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	// consumer - consume message flow

	public List<SvcMessage> processCommentEvent(CommentEvent commentEvent) {
		List<SvcMessage> messages = new ArrayList<>();
		String eventType = commentEvent.getCommentEventType().toString();
		Comment comment = commentEvent.getComment();
		String currentUser = commentEvent.getCurrentUser();
		CommentBean commentBean = null;
		if (Objects.nonNull(comment)) {

			commentBean = processAndPopulateCommentBean(comment);
			switch (eventType) {
			case "NEW":
				commentBean.setCreateDttm(new Date());
				commentBean.setUpdateDttm(new Date());
				commentBean.setLikes(0);
				List<String> likesUsernames = new ArrayList<String>();
				commentBean.setLikesUsernames(likesUsernames);
				commentRepository.save(commentBean);
				tweetRepository.incrementCommentCount(commentBean.getTweetId());
				break;
			case "UPDATE_MESSAGE":
				commentBean.setUpdateDttm(new Date());
				commentRepository.updateCommentMessage(commentBean);

				break;
			case "UPDATE_LIKE":
				commentBean.setUpdateDttm(new Date());
				commentRepository.updateCommentLike(commentBean, currentUser);
				break;

			case "DELETE":
				commentBean = commentRepository.deleteComment(commentBean);
				tweetRepository.decrementCommentCount(commentBean.getTweetId());
				break;
			default:
				LoggingUtils.logError("Error in type of event: {}", eventType);
				break;
			}
		} else {
			messages = serviceHelper.populateMessages("CMT_W_1016");
			LoggingUtils.logWarn("No Comment found for the event: {}", commentEvent.getCommentEventId().toString());
		}
		return messages;
	}

	public CommentBean processAndPopulateCommentBean(Comment comment) {
		CommentBean commentBean = new CommentBean();
		commentBean.setUsername(comment.getUsername());
		commentBean.setMessage(comment.getMessage());
		commentBean.setCreateDttm(comment.getCreateDttm());
		commentBean.setUpdateDttm(comment.getUpdateDttm());
		commentBean.setCommentId(comment.getCommentId());
		commentBean.setLikes(comment.getLikes());
		commentBean.setTweetId(comment.getTweetId());
		return commentBean;

	}

	// call producer to post message to topic
	public ResponseEntity<CommentResponse> postNewComment(CommentRequest commentRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		
		CommentEvent commentEvent = populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.NEW);
		ResponseEntity<CommentEvent> commentEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<CommentResponse> commentResponseEntity = null;
		String uniqueID = "";
		CommentResponse commentResponse = new CommentResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(commentRequest.getUsername())) {
				commentEvent.setCurrentUser(commentRequest.getUsername());
				uniqueID = commentRequest.getUsername().concat("-").concat(UUID.randomUUID().toString());
				commentEvent.getComment().setCommentId(uniqueID);
				commentEventResponse = commentEventController.postCommentEvent(commentEvent);
			} else {
				commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_W_1009));
				commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
				
			
		} catch (Exception e) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_W_1002));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (commentEventResponse.getStatusCode() == HttpStatus.CREATED) {
			commentResponse.setCommentId(uniqueID);
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_I_1001));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
		}
		return commentResponseEntity;
	}

	public ResponseEntity<CommentResponse> postUpdateComment(CommentRequest commentRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		CommentEvent commentEvent = populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.UPDATE_MESSAGE);
		ResponseEntity<CommentEvent> commentEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<CommentResponse> commentResponseEntity = null;

		CommentResponse commentResponse = new CommentResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(commentRequest.getUsername())) {
				commentEvent.setCurrentUser(commentRequest.getUsername());
				commentEventResponse = commentEventController.postCommentEvent(commentEvent);
			} else {
				commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_W_1009));
				commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_E_1006));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (commentEventResponse.getStatusCode() == HttpStatus.CREATED) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_I_1005));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
		}
		return commentResponseEntity;
	}

	public ResponseEntity<CommentResponse> postLikeComment(CommentRequest commentRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		CommentEvent commentEvent = populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.UPDATE_LIKE);
		ResponseEntity<CommentEvent> commentEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<CommentResponse> commentResponseEntity = null;

		CommentResponse commentResponse = new CommentResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(commentRequest.getUsername())) {
				commentEvent.setCurrentUser(commentRequest.getUsername());
				commentEventResponse = commentEventController.postCommentEvent(commentEvent);
			} else {
				commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_W_1009));
				commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_E_1006));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (commentEventResponse.getStatusCode() == HttpStatus.CREATED) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_I_1005));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
		}
		return commentResponseEntity;
	}

	public ResponseEntity<CommentResponse> postDeleteComment(CommentRequest commentRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		CommentEvent commentEvent = populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.DELETE);
		ResponseEntity<CommentEvent> commentEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<CommentResponse> commentResponseEntity = null;
		CommentResponse commentResponse = new CommentResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(commentRequest.getUsername())) {
				commentEvent.setCurrentUser(commentRequest.getUsername());
				commentEventResponse = commentEventController.postCommentEvent(commentEvent);
			} else {
				commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_W_1009));
				commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_E_1008));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (commentEventResponse.getStatusCode() == HttpStatus.CREATED) {
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_I_1007));
			commentResponseEntity = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
		}
		return commentResponseEntity;
	}

	private CommentEvent populateCommentEvent(CommentRequest commentRequest) {

		CommentEvent commentEvent = new CommentEvent();
		commentEvent.setCurrentUser(commentRequest.getUsername());
		Comment comment = new Comment();
		comment.setMessage(commentRequest.getMessage());
		comment.setUsername(commentRequest.getUsername());
		comment.setLikes(commentRequest.getLikes());
		comment.setLikeByUser(commentRequest.getLikeUsername());
		comment.setCommentId(commentRequest.getCommentId());
		comment.setTweetId(commentRequest.getTweetId());
		commentEvent.setComment(comment);

		return commentEvent;
	}

	// database retrieval
	public ResponseEntity<CommentResponse> retrieveAllCommentsByTweetId(CommentRequest commentRequest) {
		ResponseEntity<CommentResponse> response = null;
		CommentResponse commentResponse = null;
		List<CommentBean> commentBeans = new ArrayList<CommentBean>();
		List<Comment> comments = new ArrayList<Comment>();
		try {
			commentResponse = new CommentResponse();
			commentBeans = commentRepository.getAllCommentsByTweetId(commentRequest.getTweetId());
			for (CommentBean commentBean : commentBeans) {
				Comment comment = populateCommentUsingBean(commentBean);
				comments.add(comment);
			}
			commentResponse.setComments(comments);
			commentResponse.setTweetId(commentRequest.getTweetId());
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_I_1003));
			response = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.OK);
		} catch (Exception e) {
			commentResponse = new CommentResponse();
			commentResponse.setTweetId(commentRequest.getTweetId());
			commentResponse.setMessages(serviceHelper.populateMessages(MessageConstants.CMT_E_1004));
			response = new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	private Comment populateCommentUsingBean(CommentBean commentBean) {

		Comment comment = new Comment();
		comment.setCommentId(commentBean.getCommentId());
		comment.setMessage(commentBean.getMessage());
		comment.setCreateDttm(commentBean.getCreateDttm());
		comment.setUpdateDttm(commentBean.getUpdateDttm());
		comment.setUsername(commentBean.getUsername());
		comment.setLikes(commentBean.getLikes());
		comment.setLikeUserNames(commentBean.getLikesUsernames());
		comment.setTweetId(commentBean.getTweetId());
		return comment;
	}

}
