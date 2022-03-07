package com.peregrine.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peregrine.constant.MessageConstants;
import com.peregrine.controller.TweetController;
import com.peregrine.kafka.TweetEvent;
import com.peregrine.kafka.controller.TweetEventController;
import com.peregrine.serviceHelper.ServiceHelperForTest;
import com.peregrine.tweet.bean.TweetBean;
import com.peregrine.tweet.bean.TweetRequest;
import com.peregrine.tweet.bean.TweetResponse;

@SpringBootTest
//@EmbeddedKafka(topics = {"comment-event"}, partitions = 1, brokerProperties = {"log.dir=../target/kafka"})
@TestPropertySource(locations = "classpath:application-test.yml")
//@TestPropertySource(properties= {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
//@EnableAutoConfiguration
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class TweetEventControllerUnitTest {
	
	@Autowired
	TweetController tweetController;
	
	@Autowired
	ServiceHelperForTest serviceHelperForTest;
	
	@Value("${jwt.http.request.header}")
	private String tokenHeader; 
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Test
	public void testPostTweetSuccess() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.postTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		assertEquals(MessageConstants.TWT_I_1001, receivedCode);
		
	}
	
	@Test
	public void testPostTweetFailure_A() throws JsonProcessingException {
		//tweet posting failed due to jwt not present in header
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.postTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		assertEquals(MessageConstants.TWT_W_1002, receivedCode);
		
	}
	
	@Test
	public void testPostTweetFailure_B() throws JsonProcessingException {
		//tweet posting failed due to jwt present for another user
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.postTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot update tweet of another user
		assertEquals(MessageConstants.TWT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testPostTweetFailure_C() throws JsonProcessingException {
		//tweet posting failed due to invalid jwt
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.postTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	@Test
	public void testUpdateTweetSuccess() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.updateTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//tweet updated successfully
		assertEquals(MessageConstants.TWT_I_1005, receivedCode);
		
	}
	
	@Test
	public void testUpdateTweetFailure_A() throws JsonProcessingException {
		//tweet updating failed due to jwt not present in header
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.updateTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//error in updating comment
		assertEquals(MessageConstants.TWT_E_1006, receivedCode);
		
	}
	
	@Test
	public void testUpdateTweetFailure_B() throws JsonProcessingException {
		//tweet updating failed due to jwt present for another user
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.updateTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot update tweet of another user
		assertEquals(MessageConstants.TWT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testUpdateTweetFailure_C() throws JsonProcessingException {
		//tweet update failed due to invalid jwt
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.updateTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	@Test
	public void testLikeTweetSuccess() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.likeTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//tweet liked successfully
		assertEquals(MessageConstants.TWT_I_1005, receivedCode);
		
	}
	
	@Test
	public void testLikeTweetFailure_A() throws JsonProcessingException {
		//tweet updating failed due to jwt not present in header
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.likeTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//error in updating comment
		assertEquals(MessageConstants.TWT_E_1006, receivedCode);
		
	}
	
	@Test
	public void testLikeTweetFailure_B() throws JsonProcessingException {
		//tweet updating failed due to jwt present for another user
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.likeTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot update tweet of another user
		assertEquals(MessageConstants.TWT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testLikeTweetFailure_C() throws JsonProcessingException {
		//tweet like failed due to invalid jwt
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.likeTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	
	@Test
	public void testDeleteTweetSuccess() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.deleteTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//tweet deleted successfully
		assertEquals(MessageConstants.TWT_I_1007, receivedCode);
		
	}
	
	@Test
	public void testDeleteTweetFailure_A() throws JsonProcessingException {
		//tweet updating failed due to jwt not present in header
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.deleteTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//error in deleting tweet
		assertEquals(MessageConstants.TWT_E_1008, receivedCode);
		
	}
	
	@Test
	public void testDeleteTweetFailure_B() throws JsonProcessingException {
		//tweet updating failed due to jwt present for another user
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.deleteTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot delete tweet of another user
		assertEquals(MessageConstants.TWT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testDeleteTweetFailure_C() throws JsonProcessingException {
		//tweet delete failed due to invalid jwt
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve12345");
		
		TweetEvent tweetEvent = serviceHelperForTest.populateTweetEvent(tweetRequest);
		tweetRequest.setTweetId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		TweetEventController tweetEventController = Mockito.mock(TweetEventController.class);
		Mockito.when(tweetEventController.postTweetEvent(tweetEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(tweetEvent)); 
		ResponseEntity<TweetResponse> response = tweetController.deleteTweet(tweetRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	@Test
	public void testFetchAllTweetsError() throws JsonProcessingException {
		
		ResponseEntity<TweetResponse> response = tweetController.getAllTweets();
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//tweet retrieved successfully
		assertEquals(MessageConstants.TWT_I_1003, receivedCode);
		
	}
	
	@Test
	public void testFetchTweetsByUsernameTweetFailure() throws JsonProcessingException {
		
		TweetRequest tweetRequest = new TweetRequest();
		tweetRequest.setUsername("steve777");
		ResponseEntity<TweetResponse> response = tweetController.getAllTweetsByUser(tweetRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//tweet retrieved successfully
		assertEquals(MessageConstants.TWT_E_1004, receivedCode);
		
	}
	
	@Test
	public void testFetchTweetsByUsernameTweetFailure2() throws JsonProcessingException {
		
		ResponseEntity<TweetResponse> response = tweetController.getAllTweetsByUser(null);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//tweet retrieved successfully
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
}
