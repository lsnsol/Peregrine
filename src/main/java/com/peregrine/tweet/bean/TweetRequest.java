package com.peregrine.tweet.bean;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.constant.MessageConstants;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class TweetRequest {
	@Id
	private String id;
	private String tweetId;
	private String message;
	
	@NotEmpty(message=MessageConstants.EMPTY_UNAME)
	private String username;
	
	private Integer likes;
	private String likeUsername;
}
