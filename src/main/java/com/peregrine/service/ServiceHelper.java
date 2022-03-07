package com.peregrine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.constant.ServiceConstants;
import com.peregrine.security.jwt.JwtTokenUtil;
import com.peregrine.utils.LoggingUtils;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class ServiceHelper {

	@Autowired
	MessageSource serviceMessages;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	public List<SvcMessage> populateMessages(Object messageCodes) {
		List<SvcMessage> msgBeanList = new ArrayList<>();
		SvcMessage msgBean;
		if (messageCodes instanceof String) {
			msgBean = populateMessageBean((String) messageCodes);
			msgBeanList.add(msgBean);

		} else if (messageCodes instanceof Set<?>) {
			Set<String> codes = (Set<String>) messageCodes;
			for (String code : codes) {
				msgBean = populateMessageBean(code);
				msgBeanList.add(msgBean);
			}
		}
		return msgBeanList;
	}

	public SvcMessage populateMessageBean(String code) {

		SvcMessage messageBean = new SvcMessage();
		messageBean.setCode(code);
		messageBean.setType(getMessageType(code));
		messageBean.setMessage(serviceMessages.getMessage(code, null, Locale.US));

		return messageBean;
	}

	public String getMessageType(String code) {
		String[] codeParts = code.split("_");
		String type = ServiceConstants.INFO;
		if (codeParts != null && codeParts.length > 1) {
			// if (codeParts[1].equalsIgnoreCase(ServiceConstants.I)) {
			// type = ServiceConstants.INFO;
			// } else
			if (codeParts[1].equalsIgnoreCase(ServiceConstants.W)) {
				type = ServiceConstants.WARN;
			} else if (codeParts[1].equalsIgnoreCase(ServiceConstants.E)) {
				type = ServiceConstants.ERROR;
			}
		}
		return type;
	}

	public String getLoggedInUsername(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader(this.tokenHeader);

		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				LoggingUtils.logError("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
			} catch (ExpiredJwtException e) {
				LoggingUtils.logWarn("JWT_TOKEN_EXPIRED", e);
			}
		} else {
			LoggingUtils.logWarn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
		}
		return username;
	}
}
