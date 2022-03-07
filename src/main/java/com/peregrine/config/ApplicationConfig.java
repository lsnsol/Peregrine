package com.peregrine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.peregrine.utils.LoggingUtils;

@Component
public class ApplicationConfig {

	@Bean
	public LoggingUtils logUtils() {
		return new LoggingUtils();
	}
	
}
