package com.peregrine.tweet.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.kafka.bean.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
@Component
public class CommentResponse {

	private String tweetId;
	private String commentId;
	private	List<SvcMessage> messages;
	private	List<Comment> comments;
}
