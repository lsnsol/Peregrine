package com.peregrine.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.peregrine.constant.MessageConstants;
import com.peregrine.login.bean.LoginRequest;
import com.peregrine.login.bean.LoginResponse;
import com.peregrine.register.validator.UserValidator;
import com.peregrine.repository.ProfilesRepository;

@Service
public class LoginServiceManager {

	@Autowired
	UserValidator userValidator;

	@Autowired
	LoginOutputProcessor outputProcessor;
	
	@Autowired
	ServiceHelper serviceHelper;
	
	@Autowired
	ProfilesRepository profilesRepository;

	public ResponseEntity<LoginResponse> processLogin(LoginRequest reqBean) {

		Set<String> errorCodes = userValidator.validateLoginFields(reqBean);
		ResponseEntity<LoginResponse> response = null;

		if (CollectionUtils.isEmpty(errorCodes)) {
			// process success scenario
			response = outputProcessor.processLoginScenario(reqBean);
		} else {
			// process error scenario
			response = outputProcessor.processLoginErrorScenario(errorCodes, reqBean);
		}

		return response;
	}
	
	public ResponseEntity<LoginResponse> changePass(LoginRequest reqBean, HttpServletRequest request) {
		ResponseEntity<LoginResponse> responseEntity = null;;
		LoginResponse response = new LoginResponse();
		String loggedInUser = serviceHelper.getLoggedInUsername(request);
		try {
			if(reqBean.getUsername().equalsIgnoreCase(loggedInUser)) {
				profilesRepository.saveNewPassword(reqBean.getUsername(), reqBean.getPassword());
				response.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_I_1020));
				responseEntity = new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
			}
			else {
				response.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_W_1021));
				responseEntity = new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_E_1022));
			responseEntity = new ResponseEntity<LoginResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
