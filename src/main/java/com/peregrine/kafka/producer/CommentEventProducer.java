package com.peregrine.kafka.producer;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peregrine.kafka.CommentEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommentEventProducer {
	
	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private String topic = "comment-events";
	
	public void sendTweetEvent(CommentEvent commentEvent) throws JsonProcessingException {
		
		Integer key = commentEvent.getCommentEventId();
		String value = objectMapper.writeValueAsString(commentEvent);
		
		ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
		
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer,String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key, value, result);
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}
		});
	}
	
	public void sendCommentEventApproach(CommentEvent commentEvent) throws JsonProcessingException {
		
		Integer key = commentEvent.getCommentEventId();
		String value = objectMapper.writeValueAsString(commentEvent);
		
		ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);
		ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.send(producerRecord);
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer,String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key, value, result);
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key, value, ex);
			}
		});
	}
	
	private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
		
		List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
		return new ProducerRecord<Integer, String>(topic, null, key, value, recordHeaders);
	}

	private void handleFailure(Integer key, String value, Throwable ex) {
		log.info("Error sending the message for the key: {}, value: {}, exception: {}", key, value, ex.getMessage());
	}
	
	private void handleSuccess(Integer key, String value, SendResult<Integer,String> result) {
		log.info("Message Sent Successfully for the key: {}, value: {}, partition: {}", key, value, result.getRecordMetadata().partition());
	}
}
