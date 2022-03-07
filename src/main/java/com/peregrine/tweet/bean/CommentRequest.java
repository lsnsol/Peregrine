package com.peregrine.tweet.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class CommentRequest {
	
	private String commentId;
	private String message;
	private Date createDttm;
	private Date updateDttm;
	private String username;
	private String tweetId;
	private Integer likes;
	private String likeUsername;
}
