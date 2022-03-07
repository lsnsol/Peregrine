package com.peregrine.unit.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peregrine.kafka.TweetEvent;
import com.peregrine.kafka.TweetEventType;
import com.peregrine.service.TweetServiceManager;
import com.peregrine.serviceHelper.ServiceHelperForTest;
import com.peregrine.tweet.bean.TweetBean;
import com.peregrine.tweet.bean.TweetRequest;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
//@EnableAutoConfiguration
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class TweetConsumer {

	@Autowired
	TweetServiceManager tweetServiceManager;
	
	@Autowired
	ServiceHelperForTest serviceHelperForTest;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	
	@Test
	public void testPostNewTweetSuccess() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		tweetRequest.setMessage("Hello, I'm Steve");
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.NEW);
		tweetServiceManager.processTweetEvent(tweetEvent);
		tweetEvent.setTweetEventType(TweetEventType.DELETE);
		tweetServiceManager.processTweetEvent(tweetEvent);
		
	}
	
	@Test
	public void testPostUpdateTweetSuccess() throws JsonProcessingException {
		TweetBean tweetBean = new TweetBean();
		tweetBean.setUsername("steve12345");
		tweetBean.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		mongoTemplate.save(tweetBean);
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		tweetRequest.setMessage("Hello, I'm Steve Rogers");
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.UPDATE_MESSAGE);
		tweetServiceManager.processTweetEvent(tweetEvent);
		tweetEvent.setTweetEventType(TweetEventType.DELETE);
		tweetServiceManager.processTweetEvent(tweetEvent);
		tweetEvent.setTweetEventType(TweetEventType.DELETE);
		tweetServiceManager.processTweetEvent(tweetEvent);
	}
	
	@Test
	public void testPostUpdateLikeTweetSuccess() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		TweetBean tweetBean = new TweetBean();
		tweetBean.setUsername("steve12345");
		tweetBean.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		tweetBean.setLikes(0);
		mongoTemplate.save(tweetBean);
		
		tweetRequest.setUsername("steve12345");
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		tweetRequest.setMessage("Hello, I'm Steve Rogers");
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetEvent.setTweetEventType(TweetEventType.UPDATE_LIKE);
		tweetServiceManager.processTweetEvent(tweetEvent);
		tweetEvent.setTweetEventType(TweetEventType.DELETE);
		tweetServiceManager.processTweetEvent(tweetEvent);
	}
	
}
