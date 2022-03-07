package com.peregrine.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("local")
public class AutoCreateConfig {

	@Bean
	public NewTopic tweetEvents() {
		
		return TopicBuilder.name("tweet-events")
			.partitions(3)
			.replicas(3)
			.build();
			
	}
	
	@Bean
	public NewTopic commentEvents() {
		
		return TopicBuilder.name("comment-events")
			.partitions(3)
			.replicas(3)
			.build();
			
	}
}
