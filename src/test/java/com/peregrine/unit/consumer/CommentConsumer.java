package com.peregrine.unit.consumer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peregrine.kafka.CommentEvent;
import com.peregrine.kafka.CommentEventType;
import com.peregrine.service.CommentServiceManager;
import com.peregrine.serviceHelper.ServiceHelperForTest;
import com.peregrine.tweet.bean.CommentBean;
import com.peregrine.tweet.bean.CommentRequest;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
//@EnableAutoConfiguration
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class CommentConsumer {

	@Autowired
	CommentServiceManager commentServiceManager;
	
	@Autowired
	ServiceHelperForTest serviceHelperForTest;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	
	@Test
	public void testPostNewCommentSuccess() throws JsonProcessingException {
		
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		commentRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		commentRequest.setMessage("Hello, I'm Steve");
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.NEW);
		commentServiceManager.processCommentEvent(commentEvent);
		commentEvent.setCommentEventType(CommentEventType.DELETE);
		commentServiceManager.processCommentEvent(commentEvent);
		
	}
	
	@Test
	public void testPostUpdateCommentSuccess() throws JsonProcessingException {
		CommentBean commentBean = new CommentBean();
		commentBean.setUsername("steve12345");
		commentBean.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		commentBean.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		mongoTemplate.save(commentBean);
		
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		commentRequest.setMessage("Hello, I'm Steve Rogers");
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.UPDATE_MESSAGE);
		commentServiceManager.processCommentEvent(commentEvent);
		commentEvent.setCommentEventType(CommentEventType.DELETE);
		commentServiceManager.processCommentEvent(commentEvent);
		
	}
	
	@Test
	public void testPostUpdateLikeCommentSuccess() throws JsonProcessingException {
		
		CommentRequest commentRequest = new CommentRequest();
		CommentBean commentBean = new CommentBean();
		commentBean.setUsername("steve123");
		commentBean.setCommentId("steve123-316c4816-fbd6-4013-8f09-926308155e17");
		commentBean.setTweetId("steve123-316c4816-fbd6-4013-8f09-926308155e17");
		commentBean.setLikes(0);
		List<String> usernames = new ArrayList<>();
		commentBean.setLikesUsernames(usernames);
		mongoTemplate.save(commentBean);
		
		commentRequest.setUsername("steve123");
		commentRequest.setCommentId("steve123-316c4816-fbd6-4013-8f09-926308155e17");
		commentRequest.setMessage("Hello, I'm Steve Rogers");
		commentRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentEvent.setCommentEventType(CommentEventType.UPDATE_LIKE);
		commentServiceManager.processCommentEvent(commentEvent);
		commentEvent.setCommentEventType(CommentEventType.DELETE);
		commentServiceManager.processCommentEvent(commentEvent);
		
		
	}
	
}
