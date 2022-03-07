package com.peregrine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peregrine.constant.MessageConstants;
import com.peregrine.register.bean.RegistrationRequest;
import com.peregrine.register.bean.RegistrationResponse;
import com.peregrine.service.RegistrationServiceManager;
import com.peregrine.service.ServiceHelper;
import com.peregrine.utils.CookieUtils;

@RestController
public class RegistrationController {
	
	@Autowired
	RegistrationServiceManager registrationServiceManager;
	
	@Autowired
	CookieUtils cookieUtils;
	
	@Autowired
	ServiceHelper serviceHelper;

	@PostMapping("/api/v1.0/tweets/register")
	public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest reqBean) {
		
		ResponseEntity<RegistrationResponse> response = registrationServiceManager.processRegistration(reqBean);
		if(response == null) {
			response = new ResponseEntity<RegistrationResponse>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@PostMapping("/api/v1.0/tweets/username")
	public ResponseEntity<RegistrationResponse> checkUsername(@RequestBody RegistrationRequest reqBean) {
		
		ResponseEntity<RegistrationResponse> response = registrationServiceManager.checkUserName(reqBean);
		if(response == null) {
			response = new ResponseEntity<RegistrationResponse>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@PostMapping("/api/v1.0/tweets/email")
	public ResponseEntity<RegistrationResponse> checkEmail(@RequestBody RegistrationRequest reqBean) {
		
		ResponseEntity<RegistrationResponse> response = registrationServiceManager.checkEmail(reqBean);
		if(response == null) {
			response = new ResponseEntity<RegistrationResponse>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	
	@PostMapping("/getToken")
	public ResponseEntity<RegistrationResponse> getToken(@RequestBody RegistrationRequest reqBean) {
		
		RegistrationResponse responseBean = new RegistrationResponse();
		HttpHeaders httpHeaders = cookieUtils.setCookiesPostAuthentication(reqBean);
		ResponseEntity<RegistrationResponse> response = null;
		if(httpHeaders.isEmpty()) {
			responseBean.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_W_1014)); 
		}
		response = new ResponseEntity<>(responseBean, httpHeaders, HttpStatus.OK);
		return response;
	}
}
