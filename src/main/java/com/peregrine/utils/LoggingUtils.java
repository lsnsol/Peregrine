package com.peregrine.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peregrine.constant.NumberConstants;

public class LoggingUtils {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingUtils.class);
	
	public static void logDebug(String log) {
		if(LOG.isDebugEnabled()) {
			LOG.debug(log);
		}
	}
	
	public static void logWarn(String log) {
		if(LOG.isWarnEnabled()) {
			LOG.warn(log);
		}
	}
	
	public static void logWarn(String log, String arg) {
		if(LOG.isWarnEnabled()) {
			LOG.warn(log, arg);
		}
	}
	
	public static void logError(String log) {
		if(LOG.isErrorEnabled()) {
			LOG.error(log);
		}
	}
	
	public static void logError(String log, String arg) {
		if(LOG.isErrorEnabled()) {
			LOG.error(log, arg);
		}
	}
	
	public static void logError(String log, Exception e) {
		if(LOG.isErrorEnabled()) {
			LOG.error(log, shortenedStackTrace(e, NumberConstants.NUM_10));
		}
	}
	
	public static void logWarn(String log, Exception e) {
		if(LOG.isWarnEnabled()) {
			LOG.warn(log, shortenedStackTrace(e, NumberConstants.NUM_10));
		}
	}
	
	public static String shortenedStackTrace(Throwable e, int maxLines) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String[] lines = writer.toString().split("\n");
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < Math.min(lines.length, maxLines); i++) {
			sb.append(lines[i]).append("\n");
		}
		return sb.toString();
	}
}
