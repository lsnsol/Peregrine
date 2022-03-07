package com.peregrine.kafka;

import com.peregrine.kafka.bean.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentEvent {
	
	private Integer commentEventId;
	private CommentEventType commentEventType;
	private String currentUser;
	private Comment comment;
}
