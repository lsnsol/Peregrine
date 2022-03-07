package com.peregrine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.constant.MessageConstants;
import com.peregrine.login.bean.LoginRequest;
import com.peregrine.login.bean.LoginResponse;
import com.peregrine.register.bean.PeregrineUser;
import com.peregrine.security.jwt.JwtInMemoryUserDetailsService;
import com.peregrine.utils.CommonUtils;
import com.peregrine.utils.CookieUtils;
import com.peregrine.utils.LoggingUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class LoginOutputProcessor {

	@Autowired
	CookieUtils cookieUtils;

	@Autowired
	ProfileRegistrationManager profileRegistrationManager;

	@Autowired
	LoggingUtils loggingUtils;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	ServiceHelper serviceHelper;

	public ResponseEntity<LoginResponse> processLoginScenario(LoginRequest reqBean) {

		LoginResponse loginResponseBean = new LoginResponse();

		HttpHeaders updatedHeaders = new HttpHeaders();

		PeregrineUser registeredProfile = null;
		ResponseEntity<LoginResponse> response = new ResponseEntity<>(loginResponseBean, HttpStatus.OK);

		try {

			// fetch profile from db post registration
			List<PeregrineUser> profiles = profileRegistrationManager.findUserByUsername(reqBean.getUsername());
			if (!CollectionUtils.isEmpty(profiles)) {
				registeredProfile = profiles.get(0);
				registeredProfile.setPassword("**********");

				// inject user from db into the jwt security
				JwtInMemoryUserDetailsService.addInMemoryUserList(1L, registeredProfile.getUsername(),
						encodePasswordInLoginBean(reqBean));

			} else
				throw new Exception("Error in fetching user from db");

			// add jwt cookies
			updatedHeaders = cookieUtils.setCookiesPostAuthentication(reqBean);

			if (updatedHeaders.isEmpty()) {
				loginResponseBean.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_W_1014));
				response = new ResponseEntity<>(loginResponseBean, updatedHeaders, HttpStatus.OK);
			} else {
				// registered successfully messages
				List<SvcMessage> messages = new ArrayList<>();
				messages.add(serviceHelper.populateMessageBean(MessageConstants.LOG_I_1017));
				loginResponseBean.setMessages(messages);

				loginResponseBean.setProfile(registeredProfile);
				response = new ResponseEntity<>(loginResponseBean, updatedHeaders, HttpStatus.OK);
			}

		} catch (Exception e) {
			// add logs for error in login post registration
			LoggingUtils.logError("LOGIN_ERROR - Error in LOGIN process", e);
			loginResponseBean.setMessages(serviceHelper.populateMessages(MessageConstants.SVC_UNAVAILABLE));
			response = new ResponseEntity<>(loginResponseBean, updatedHeaders, HttpStatus.OK);
		}

		return response;
	}

	public ResponseEntity<LoginResponse> processLoginErrorScenario(Set<String> errorCodes, LoginRequest reqBean) {
		LoginResponse responseBean = new LoginResponse();

		responseBean.setUsername(reqBean.getUsername());

		commonUtils.handleErrorMessageInLoginService(errorCodes, responseBean);

		return new ResponseEntity<>(responseBean, HttpStatus.OK);
	}

	private String encodePasswordInLoginBean(LoginRequest reqBean) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(reqBean.getPassword());
	}
}
