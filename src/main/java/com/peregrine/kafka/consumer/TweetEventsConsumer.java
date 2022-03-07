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
import com.peregrine.kafka.TweetEvent;
import com.peregrine.service.TweetServiceManager;
import com.peregrine.utils.LoggingUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TweetEventsConsumer implements AcknowledgingMessageListener<Integer, String> {

	@Autowired
	TweetServiceManager tweetServiceManager;
	
	@Override
	@KafkaListener(topics= {"tweet-events"})
	public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
		log.info("ConsumerRecord: {}", data);
		log.info("Key: {}", data.key());
		log.info("Value: {}", data.value());
		List<SvcMessage> messages = new ArrayList<>();
		TweetEvent tweetEventFromTopic = retrieveTweetEventFromTopic(data);
		if(Objects.nonNull(tweetEventFromTopic)) {
			messages = tweetServiceManager.processTweetEvent(tweetEventFromTopic);
		}
		acknowledgment.acknowledge();
	}

	private TweetEvent retrieveTweetEventFromTopic(ConsumerRecord<Integer, String> data) {
		String value = data.value();
		TweetEvent tweetEvent = null;
		try {
			if(StringUtils.isNotBlank(value)) {
				ObjectMapper mapper = new ObjectMapper();
				tweetEvent = mapper.readValue(value, TweetEvent.class);
			}
		} catch (Exception e) {
			tweetEvent = null;
			LoggingUtils.logError("CONSUMER_FATAL_ERROR - Error in mapping message from topic" , e);
		}
		return tweetEvent;
	}
}
