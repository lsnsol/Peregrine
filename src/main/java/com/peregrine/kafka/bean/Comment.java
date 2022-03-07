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
public class Comment {
	private String commentId;
	private String message;
	private Date createDttm;
	private Date updateDttm;
	private String username;
	private String tweetId;
	
	private Integer likes;
	@JsonIgnore
	private String likeByUser;
	private List<String> likeUserNames;
}
