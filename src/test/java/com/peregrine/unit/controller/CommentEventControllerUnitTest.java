package com.peregrine.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.peregrine.constant.MessageConstants;
import com.peregrine.controller.CommentController;
import com.peregrine.kafka.CommentEvent;
import com.peregrine.kafka.controller.CommentEventController;
import com.peregrine.serviceHelper.ServiceHelperForTest;
import com.peregrine.tweet.bean.CommentRequest;
import com.peregrine.tweet.bean.CommentResponse;

@SpringBootTest
//@EmbeddedKafka(topics = {"comment-event"}, partitions = 1, brokerProperties = {"log.dir=../target/kafka"})
@TestPropertySource(locations = "classpath:application-test.yml")
//@TestPropertySource(properties= {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
//@EnableAutoConfiguration
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class CommentEventControllerUnitTest {
	
	@Autowired
	CommentController commentController;
	
	@InjectMocks
	CommentController commentEventController;
	
	@Autowired
	ServiceHelperForTest serviceHelperForTest;
	
	@Value("${jwt.http.request.header}")
	private String tokenHeader;
	
	@Test
	public void testPostCommentSuccess() throws JsonProcessingException {
		
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.postComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		assertEquals(MessageConstants.CMT_I_1001, receivedCode);
		
	}
	
	@Test
	public void testPostCommentFailure_A() throws JsonProcessingException {
		//comment posting failed due to jwt not present in header
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.postComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		assertEquals(MessageConstants.CMT_W_1002, receivedCode);
		
	}
	
	@Test
	public void testPostCommentFailure_B() throws JsonProcessingException {
		//comment posting failed due to jwt present for another user
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.postComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot update comment of another user
		assertEquals(MessageConstants.CMT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testPostCommentFailure_C() throws JsonProcessingException {
		//comment posting failed due to invalid jwt
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.postComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	@Test
	public void testUpdateCommentSuccess() throws JsonProcessingException {
		
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.updateComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//comment updated successfully
		assertEquals(MessageConstants.CMT_I_1005, receivedCode);
		
	}
	
	@Test
	public void testUpdateCommentFailure_A() throws JsonProcessingException {
		//comment updating failed due to jwt not present in header
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.updateComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//error in updating comment
		assertEquals(MessageConstants.CMT_E_1006, receivedCode);
		
	}
	
	@Test
	public void testUpdateCommentFailure_B() throws JsonProcessingException {
		//comment updating failed due to jwt present for another user
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.updateComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot update comment of another user
		assertEquals(MessageConstants.CMT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testUpdateCommentFailure_C() throws JsonProcessingException {
		//comment update failed due to invalid jwt
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.updateComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	@Test
	public void testLikeCommentSuccess() throws JsonProcessingException {
		
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.likeComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//comment liked successfully
		assertEquals(MessageConstants.CMT_I_1005, receivedCode);
		
	}
	
	@Test
	public void testLikeCommentFailure_A() throws JsonProcessingException {
		//comment updating failed due to jwt not present in header
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.likeComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//error in updating comment
		assertEquals(MessageConstants.CMT_E_1006, receivedCode);
		
	}
	
	@Test
	public void testLikeCommentFailure_B() throws JsonProcessingException {
		//comment updating failed due to jwt present for another user
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.likeComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot update comment of another user
		assertEquals(MessageConstants.CMT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testLikeCommentFailure_C() throws JsonProcessingException {
		//comment like failed due to invalid jwt
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.likeComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	
	@Test
	public void testDeleteCommentSuccess() throws JsonProcessingException {
		
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGV2ZTEyMzQ1IiwiZXhwIjoxNjQ1NTM5MDAzLCJpYXQiOjE2NDQ5MzQyMDN9.dLl5cJPV5N4pcS7fS02Ur8IoCPN4g1FvL5ZMLUpGpwtEcb3jYB7g7Amd8HNWSIcg88KYDhG9RLtn7c59QsfjKA");
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.deleteComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//comment deleted successfully
		assertEquals(MessageConstants.CMT_I_1007, receivedCode);
		
	}
	
	@Test
	public void testDeleteCommentFailure_A() throws JsonProcessingException {
		//comment updating failed due to jwt not present in header
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.deleteComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//error in deleting comment
		assertEquals(MessageConstants.CMT_E_1008, receivedCode);
		
	}
	
	@Test
	public void testDeleteCommentFailure_B() throws JsonProcessingException {
		//comment updating failed due to jwt present for another user
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ1NTM5NDQ2LCJpYXQiOjE2NDQ5MzQ2NDZ9.XMTih-7BbCY42kOJDOpl7_gq-epX-_emnIwYRTU7n3ZrTNRKQdmyRUfqq265WQmK0JssFOXkza5liovAlDk7Iw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.deleteComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//cannot delete comment of another user
		assertEquals(MessageConstants.CMT_W_1009, receivedCode);
		
	}
	
	@Test
	public void testDeleteCommentFailure_C() throws JsonProcessingException {
		//comment delete failed due to invalid jwt
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setUsername("steve12345");
		
		CommentEvent commentEvent = serviceHelperForTest.populateCommentEvent(commentRequest);
		commentRequest.setCommentId("steve1234-316c4816-fbd6-4013-8f09-926308155e17");
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		
		Mockito.when(mockedRequest.getHeader(this.tokenHeader)).thenReturn("Bearer xxxxiJIUzUxMiJ9.eyJzdWIiOiJzb2hyYWJhemFtMTIzIiwiZXhwIjoxNjQ0OTE1MDA4LCJpYXQiOjE2NDQzMTAyMDh9.7vpl4MBxK7BUwNCAIEmA4O_-PggiIk9UUqsgM_PSW5aNIa-fC74ADxLE398384rWxfW115mXrvpX-qhrdRzzuw"); 
		CommentEventController commentEventController = Mockito.mock(CommentEventController.class);
		Mockito.when(commentEventController.postCommentEvent(commentEvent)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentEvent)); 
		ResponseEntity<CommentResponse> response = commentController.deleteComment(commentRequest, mockedRequest);
		String receivedCode = response.getBody().getMessages().get(0).getCode();
		//invalid jwt
		assertEquals(MessageConstants.SVC_UNAVAILABLE, receivedCode);
		
	}
	
	
	
}
