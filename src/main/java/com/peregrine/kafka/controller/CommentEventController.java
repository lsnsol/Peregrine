package com.peregrine.kafka.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peregrine.kafka.CommentEvent;
import com.peregrine.kafka.producer.CommentEventProducer;

@RestController
public class CommentEventController {
	
	@Autowired
	CommentEventProducer commentEventProducer;
	
	@Value("${isTestEnabled}")
	private Boolean isTestEnabled;

	@PostMapping("/v1/commentEvent")
	public ResponseEntity<CommentEvent> postCommentEvent(@RequestBody CommentEvent commentEvent) throws JsonProcessingException {
		if(isTestEnabled) {
			return ResponseEntity.status(HttpStatus.CREATED).body(commentEvent);
		}
		commentEvent.getComment().setCreateDttm(new Date());
		//invoke kafka producer
		commentEventProducer.sendCommentEventApproach(commentEvent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(commentEvent);
	}
	
}
