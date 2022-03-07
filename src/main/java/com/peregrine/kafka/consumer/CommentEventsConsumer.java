package com.peregrine.kafka.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.kafka.CommentEvent;
import com.peregrine.service.CommentServiceManager;
import com.peregrine.utils.LoggingUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommentEventsConsumer implements AcknowledgingMessageListener<Integer, String> {

	@Autowired
	CommentServiceManager commentServiceManager;
	
	@Override
	@KafkaListener(topics= {"comment-events"})
	public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
		log.info("ConsumerRecord: {}", data);
		log.info("Key: {}", data.key());
		log.info("Value: {}", data.value());
		List<SvcMessage> messages = new ArrayList<>();
		CommentEvent commentEventFromTopic = retrieveCommentEventFromTopic(data);
		if(Objects.nonNull(commentEventFromTopic)) {
			messages = commentServiceManager.processCommentEvent(commentEventFromTopic);
		}
		acknowledgment.acknowledge();
	}

	private CommentEvent retrieveCommentEventFromTopic(ConsumerRecord<Integer, String> data) {
		String value = data.value();
		CommentEvent commentEvent = null;
		try {
			if(StringUtils.isNotBlank(value)) {
				ObjectMapper mapper = new ObjectMapper();
				commentEvent = mapper.readValue(value, CommentEvent.class);
			}
		} catch (Exception e) {
			commentEvent = null;
			LoggingUtils.logError("CONSUMER_FATAL_ERROR - Error in mapping message from topic" , e);
		}
		return commentEvent;
	}
}
