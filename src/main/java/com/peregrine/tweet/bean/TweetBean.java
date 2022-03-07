package com.peregrine.tweet.bean;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document(collection = "tweetData")
public class TweetBean {

	@Id
	private String id;
	private String tweetId;
	private String message;
	private Date createDttm;
	private Date updateDttm;
	private String username;
	private Integer likes;
	private List<String> likesUsernames;
	private String avatar;
	private Long noOfComments;
}
