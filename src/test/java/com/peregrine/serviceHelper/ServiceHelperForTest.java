package com.peregrine.serviceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.constant.ServiceConstants;
import com.peregrine.kafka.CommentEvent;
import com.peregrine.kafka.TweetEvent;
import com.peregrine.kafka.bean.Comment;
import com.peregrine.kafka.bean.Tweet;
import com.peregrine.tweet.bean.CommentRequest;
import com.peregrine.tweet.bean.TweetRequest;

@Service
public class ServiceHelperForTest {
	
	@Autowired
	MessageSource serviceMessages;
	
	public TweetEvent populateTweetEvent(TweetRequest tweetRequest) {

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
	
	public CommentEvent populateCommentEvent(CommentRequest commentRequest) {

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
	
	public List<SvcMessage> populateMessages(Object messageCodes) {
		List<SvcMessage> msgBeanList = new ArrayList<>();
		SvcMessage msgBean;
		if (messageCodes instanceof String) {
			msgBean = populateMessageBean((String) messageCodes);
			msgBeanList.add(msgBean);

		} else if (messageCodes instanceof Set<?>) {
			Set<String> codes = (Set<String>) messageCodes;
			for (String code : codes) {
				msgBean = populateMessageBean(code);
				msgBeanList.add(msgBean);
			}
		}
		return msgBeanList;
	}

	public SvcMessage populateMessageBean(String code) {

		SvcMessage messageBean = new SvcMessage();
		messageBean.setCode(code);
		messageBean.setType(getMessageType(code));
		messageBean.setMessage(serviceMessages.getMessage(code, null, Locale.US));

		return messageBean;
	}

	public String getMessageType(String code) {
		String[] codeParts = code.split("_");
		String type = ServiceConstants.INFO;
		if (codeParts != null && codeParts.length > 1) {
			// if (codeParts[1].equalsIgnoreCase(ServiceConstants.I)) {
			// type = ServiceConstants.INFO;
			// } else
			if (codeParts[1].equalsIgnoreCase(ServiceConstants.W)) {
				type = ServiceConstants.WARN;
			} else if (codeParts[1].equalsIgnoreCase(ServiceConstants.E)) {
				type = ServiceConstants.ERROR;
			}
		}
		return type;
	}

}
