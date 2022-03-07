package com.peregrine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.constant.MessageConstants;
import com.peregrine.kafka.TweetEvent;
import com.peregrine.kafka.TweetEventType;
import com.peregrine.kafka.bean.Tweet;
import com.peregrine.kafka.controller.TweetEventController;
import com.peregrine.register.bean.PeregrineUser;
import com.peregrine.repository.CommentRepository;
import com.peregrine.repository.ProfilesRepository;
import com.peregrine.repository.TweetRepository;
import com.peregrine.security.jwt.JwtTokenUtil;
import com.peregrine.tweet.bean.TweetBean;
import com.peregrine.tweet.bean.TweetRequest;
import com.peregrine.tweet.bean.TweetResponse;
import com.peregrine.utils.LoggingUtils;

@Service
public class TweetServiceManager {

	@Autowired
	ServiceHelper serviceHelper;

	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	ProfilesRepository profilesRepository;

	@Autowired
	TweetEventController tweetEventController;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	// consumer - consume message flow

	public List<SvcMessage> processTweetEvent(TweetEvent tweetEvent) {
		List<SvcMessage> messages = new ArrayList<>();
		String eventType = tweetEvent.getTweetEventType().toString();
		Tweet tweet = tweetEvent.getTweet();
		String currentUser = tweetEvent.getCurrentUser();
		TweetBean tweetBean = null;
		if (Objects.nonNull(tweet)) {

			tweetBean = processAndPopulateTweetBean(tweet);
			switch (eventType) {
			case "NEW":
				tweetBean.setCreateDttm(new Date());
				tweetBean.setUpdateDttm(new Date());
				tweetBean.setLikes(0);
				List<String> likesUsernames = new ArrayList<String>();
				tweetBean.setLikesUsernames(likesUsernames);
				tweetBean.setNoOfComments(0L);
				tweetRepository.save(tweetBean);
				break;
			case "UPDATE_MESSAGE":
				tweetBean.setUpdateDttm(new Date());
				tweetRepository.updateTweetMessage(tweetBean);

				break;
			case "UPDATE_LIKE":
				tweetBean.setUpdateDttm(new Date());
				tweetRepository.updateTweetLike(tweetBean, currentUser);
				break;

			case "DELETE":
				tweetRepository.deleteTweet(tweetBean);
				commentRepository.deleteCommentsByTweetId(tweetBean.getTweetId());
				break;
			default:
				LoggingUtils.logError("Error in type of event: {}", eventType);
				break;
			}
		} else {
			messages = serviceHelper.populateMessages("TWT_W_1016");
			LoggingUtils.logWarn("No Tweet found for the event: {}", tweetEvent.getTweetEventId().toString());
		}
		return messages;
	}

	public TweetBean processAndPopulateTweetBean(Tweet tweet) {
		TweetBean tweetBean = new TweetBean();
		tweetBean.setUsername(tweet.getUsername());
		tweetBean.setMessage(tweet.getMessage());
		tweetBean.setCreateDttm(tweet.getCreateDttm());
		tweetBean.setUpdateDttm(tweet.getUpdateDttm());
		tweetBean.setTweetId(tweet.getTweetId());
		tweetBean.setLikes(tweet.getLikes());
		return tweetBean;

	}

	// call producer to post message to topic
	public ResponseEntity<TweetResponse> postNewTweet(TweetRequest tweetRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		
		TweetEvent tweetEvent = populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.NEW);
		ResponseEntity<TweetEvent> tweetEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<TweetResponse> tweetResponseEntity = null;
		String uniqueID = "";
		TweetResponse tweetResponse = new TweetResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(tweetRequest.getUsername())) {
				tweetEvent.setCurrentUser(tweetRequest.getUsername());
				uniqueID = tweetRequest.getUsername().concat("-").concat(UUID.randomUUID().toString());
				tweetEvent.getTweet().setTweetId(uniqueID);
				tweetEventResponse = tweetEventController.postTweetEvent(tweetEvent);
			} else {
				tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_W_1009));
				tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
				
			
		} catch (Exception e) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_W_1002));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (tweetEventResponse.getStatusCode() == HttpStatus.CREATED) {
			tweetResponse.setTweetId(uniqueID);
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_I_1001));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.OK);
		}
		return tweetResponseEntity;
	}

	public ResponseEntity<TweetResponse> postUpdateTweet(TweetRequest tweetRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		TweetEvent tweetEvent = populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.UPDATE_MESSAGE);
		ResponseEntity<TweetEvent> tweetEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<TweetResponse> tweetResponseEntity = null;

		TweetResponse tweetResponse = new TweetResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(tweetRequest.getUsername())) {
				tweetEvent.setCurrentUser(tweetRequest.getUsername());
				tweetEventResponse = tweetEventController.postTweetEvent(tweetEvent);
			} else {
				tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_W_1009));
				tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_E_1006));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (tweetEventResponse.getStatusCode() == HttpStatus.CREATED) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_I_1005));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.OK);
		}
		return tweetResponseEntity;
	}

	public ResponseEntity<TweetResponse> postLikeTweet(TweetRequest tweetRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		TweetEvent tweetEvent = populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.UPDATE_LIKE);
		ResponseEntity<TweetEvent> tweetEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<TweetResponse> tweetResponseEntity = null;

		TweetResponse tweetResponse = new TweetResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(tweetRequest.getUsername())) {
				tweetEvent.setCurrentUser(tweetRequest.getUsername());
				tweetEventResponse = tweetEventController.postTweetEvent(tweetEvent);
			} else {
				tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_W_1009));
				tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_E_1006));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (tweetEventResponse.getStatusCode() == HttpStatus.CREATED) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_I_1005));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.OK);
		}
		return tweetResponseEntity;
	}

	public ResponseEntity<TweetResponse> postDeleteTweet(TweetRequest tweetRequest, HttpServletRequest request) {
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		TweetEvent tweetEvent = populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.DELETE);
		ResponseEntity<TweetEvent> tweetEventResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<TweetResponse> tweetResponseEntity = null;
		TweetResponse tweetResponse = new TweetResponse();
		try {
			if(loggedInUser.equalsIgnoreCase(tweetRequest.getUsername())) {
				tweetEvent.setCurrentUser(tweetRequest.getUsername());
				tweetEventResponse = tweetEventController.postTweetEvent(tweetEvent);
			} else {
				tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_W_1009));
				tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_E_1008));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (tweetEventResponse.getStatusCode() == HttpStatus.CREATED) {
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_I_1007));
			tweetResponseEntity = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.OK);
		}
		return tweetResponseEntity;
	}

	private TweetEvent populateTweetEvent(TweetRequest tweetRequest) {

		TweetEvent tweetEvent = new TweetEvent();
		tweetEvent.setCurrentUser(tweetRequest.getUsername());
		Tweet tweet = new Tweet();
		tweet.setMessage(tweetRequest.getMessage());
		tweet.setUsername(tweetRequest.getUsername());
		tweet.setLikes(tweetRequest.getLikes());
		tweet.setLikeByUser(tweetRequest.getLikeUsername());
		tweet.setTweetId(tweetRequest.getTweetId());
		tweetEvent.setTweet(tweet);

		return tweetEvent;
	}

	// database retrieval
	public ResponseEntity<TweetResponse> retrieveAllTweetsByUser(TweetRequest tweetRequest) {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = null;
		List<TweetBean> tweetBeans = new ArrayList<TweetBean>();
		List<Tweet> tweets = new ArrayList<Tweet>();
		List<PeregrineUser> user = null;
		String avatar = "";
		try {
			tweetResponse = new TweetResponse();
			tweetBeans = tweetRepository.getAllTweetsByUsername(tweetRequest.getUsername());
			user = profilesRepository.findByUsername(tweetRequest.getUsername());
			avatar = user.get(0).getAvatar();
			for (TweetBean tweetBean : tweetBeans) {
				Tweet tweet = populateTweetUsingBean(tweetBean);
				tweet.setAvatar(avatar);
				tweets.add(tweet);
			}
			tweetResponse.setTweets(tweets);
			tweetResponse.setUsername(tweetRequest.getUsername());
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_I_1003));
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.OK);
		} catch (Exception e) {
			tweetResponse = new TweetResponse();
			tweetResponse.setUsername(tweetRequest.getUsername());
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_E_1004));
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	// database retrieval

	public ResponseEntity<TweetResponse> retrieveAllTweets() {
		ResponseEntity<TweetResponse> response = null;
		TweetResponse tweetResponse = null;
		List<TweetBean> tweetBeans = new ArrayList<TweetBean>();
		List<Tweet> tweets = new ArrayList<Tweet>();
		List<PeregrineUser> users = null;
		Set<String> usernames = new HashSet<>();
		PeregrineUser user;
		String avatar = "";
		try {
			tweetResponse = new TweetResponse();
			tweetBeans = tweetRepository.getAllTweets();
			usernames = tweetBeans.stream().map(TweetBean::getUsername).collect(Collectors.toSet());
			users = profilesRepository.findByUsernamesList(usernames);
			for (TweetBean tweetBean : tweetBeans) {
				user = users.stream().filter(f -> f.getUsername().equalsIgnoreCase(tweetBean.getUsername())).findFirst().get();
				avatar = user.getAvatar();
				Tweet tweet = populateTweetUsingBean(tweetBean);
				tweet.setAvatar(avatar);
				tweets.add(tweet);
			}
			tweetResponse.setTweets(tweets);
			tweetResponse.setUsername("");
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_I_1003));
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.OK);
		} catch (Exception e) {
			tweetResponse = new TweetResponse();
			tweetResponse.setUsername("");
			tweetResponse.setMessages(serviceHelper.populateMessages(MessageConstants.TWT_E_1004));
			response = new ResponseEntity<TweetResponse>(tweetResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	private Tweet populateTweetUsingBean(TweetBean tweetBean) {

		Tweet tweet = new Tweet();
		tweet.setTweetId(tweetBean.getTweetId());
		tweet.setMessage(tweetBean.getMessage());
		tweet.setCreateDttm(tweetBean.getCreateDttm());
		tweet.setUpdateDttm(tweetBean.getUpdateDttm());
		tweet.setUsername(tweetBean.getUsername());
		tweet.setLikes(tweetBean.getLikes());
		tweet.setLikeUserNames(tweetBean.getLikesUsernames());
		tweet.setAvatar(tweetBean.getAvatar());
		tweet.setNoOfComments(tweetBean.getNoOfComments());
		return tweet;
	}

}
