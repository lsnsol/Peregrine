package com.peregrine.kafka.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Tweet {

	private String tweetId;
	private String message;
	private Date createDttm;
	private Date updateDttm;
	private String username;
	private Integer likes;
	@JsonIgnore
	private String likeByUser;
	private List<String> likeUserNames;
	private String avatar;
	private Long noOfComments;
	
}
