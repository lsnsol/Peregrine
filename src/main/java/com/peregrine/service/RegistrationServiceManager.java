package com.peregrine.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.peregrine.register.bean.RegistrationRequest;
import com.peregrine.register.bean.RegistrationResponse;
import com.peregrine.register.validator.UserValidator;

@Service
public class RegistrationServiceManager {
	
	@Autowired
	ProfileRegistrationManager profileRegistrationManager; 
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	RegistrationOutputProcessor outputProcessor;
	
	public ResponseEntity<RegistrationResponse> processRegistration(RegistrationRequest reqBean) {
		
		Set<String> errorCodes = userValidator.validateRegFields(reqBean);
		ResponseEntity<RegistrationResponse> response = null;
		
		if(CollectionUtils.isEmpty(errorCodes)) {
			// trim extra spaces
			
			
			// responseEntity from repo
			response = registrationInRepo(reqBean);
		} else {
			// process error scenario
			response = outputProcessor.processRegErrorScenario(errorCodes, reqBean);
		}
		
		return response;
	}

	private ResponseEntity<RegistrationResponse> registrationInRepo(RegistrationRequest reqBean) {
		ResponseEntity<RegistrationResponse> response = null;
		
		//save user in collection
		profileRegistrationManager.processUserCreation(reqBean);
		
		//process success scenario
		response = outputProcessor.processRegSucessScenario(reqBean);
		
		return response;
	}
	
	public ResponseEntity<RegistrationResponse> checkUserName(RegistrationRequest reqBean) {
		
		ResponseEntity<RegistrationResponse> response = null;
		
		response = outputProcessor.processUsernameExistsScenario(reqBean);
		
		return response;
		
	}
	
	public ResponseEntity<RegistrationResponse> checkEmail(RegistrationRequest reqBean) {
		
		ResponseEntity<RegistrationResponse> response = null;
		
		response = outputProcessor.processEmailExistsScenario(reqBean);
		
		return response;
		
	}
}
