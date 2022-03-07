package com.peregrine.tweet.bean;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
@Document(collection = "commentData")
public class CommentBean {

	@Id
	private String id;
	private String commentId;
	private String message;
	private Date createDttm;
	private Date updateDttm;
	private String username;
	private String tweetId;
	private Integer likes;
	private List<String> likesUsernames;
}
