package com.peregrine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.constant.MessageConstants;
import com.peregrine.register.bean.PeregrineUser;
import com.peregrine.register.bean.RegistrationRequest;
import com.peregrine.register.bean.RegistrationResponse;
import com.peregrine.security.jwt.JwtInMemoryUserDetailsService;
import com.peregrine.utils.CommonUtils;
import com.peregrine.utils.CookieUtils;
import com.peregrine.utils.LoggingUtils;

@Service
public class RegistrationOutputProcessor {
	
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
	
	public ResponseEntity<RegistrationResponse> processRegSucessScenario(RegistrationRequest reqBean) {
		
		RegistrationResponse regResponseBean = new RegistrationResponse();
		
		HttpHeaders updatedHeaders = new HttpHeaders();
		
		ResponseEntity<RegistrationResponse> response = new ResponseEntity<>(regResponseBean, HttpStatus.OK);
		
		try {
			
			//fetch profile from db post registration
			List<PeregrineUser> profiles = profileRegistrationManager.findUserByUsername(reqBean.getUsername());
			if(!CollectionUtils.isEmpty(profiles)) {
				PeregrineUser registeredProfile = profiles.get(0);
				
				//inject user from db into the jwt security
				JwtInMemoryUserDetailsService.addInMemoryUserList(1L, registeredProfile.getUsername(), encodePasswordInRegBean(reqBean));
				
			}
			else throw new Exception("Error in fetching user from db");
			
			//add jwt cookies
			updatedHeaders = cookieUtils.setCookiesPostAuthentication(reqBean);
			
			if(updatedHeaders.isEmpty()) {
				regResponseBean.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_W_1014));
				response = new ResponseEntity<> (regResponseBean, updatedHeaders, HttpStatus.OK);
			} else {
				//registered successfully messages
				List<SvcMessage> messages = new ArrayList<>();
				messages.add(serviceHelper.populateMessageBean(MessageConstants.REG_I_1014));
				messages.add(serviceHelper.populateMessageBean(MessageConstants.LOG_I_1017));
				regResponseBean.setMessages(messages);
				response = new ResponseEntity<> (regResponseBean, updatedHeaders, HttpStatus.OK);
			}
			
			
		} catch (Exception e) {
			//add logs for error in login post registration
			regResponseBean.setMessages(serviceHelper.populateMessages(MessageConstants.LOG_W_1015));
			response = new ResponseEntity<> (regResponseBean, updatedHeaders, HttpStatus.OK);
			LoggingUtils.logError("REGISTRATION_ERROR - Error in Registration process", e);
		}
		
		return response;
	}
	
	
	public ResponseEntity<RegistrationResponse> processRegErrorScenario(Set<String> errorCodes, RegistrationRequest reqBean) {
		RegistrationResponse responseBean = new RegistrationResponse();
		
		responseBean.setUsername(reqBean.getUsername());
		
		commonUtils.handleErrorMessageInRegService(errorCodes, responseBean);
		
		ResponseEntity<RegistrationResponse> response = new ResponseEntity<>(responseBean, HttpStatus.OK);
		
		return response;
		
		
	}
	
	public ResponseEntity<RegistrationResponse> processUsernameExistsScenario(RegistrationRequest reqBean) {
		RegistrationResponse responseBean = new RegistrationResponse();
		
		responseBean.setUsername(reqBean.getUsername());
		
		try {
			List<PeregrineUser> profiles = profileRegistrationManager.findUserByUsername(reqBean.getUsername());
			
			if(CollectionUtils.isEmpty(profiles)) {
				responseBean.setUsernameExists(false);
			} else {
				responseBean.setUsernameExists(true);
			}
		} catch (Exception e) {
			
		}
		
		ResponseEntity<RegistrationResponse> response = new ResponseEntity<>(responseBean, HttpStatus.OK);
		
		return response;
		
		
	}
	
	public ResponseEntity<RegistrationResponse> processEmailExistsScenario(RegistrationRequest reqBean) {
		RegistrationResponse responseBean = new RegistrationResponse();
		
		responseBean.setEmail(reqBean.getEmail());
		
		try {
			List<PeregrineUser> profiles = profileRegistrationManager.findUserByEmail(reqBean.getEmail());
			
			if(CollectionUtils.isEmpty(profiles)) {
				responseBean.setEmailExists(false);
			} else {
				responseBean.setEmailExists(true);
			}
		} catch (Exception e) {
			
		}
		
		ResponseEntity<RegistrationResponse> response = new ResponseEntity<>(responseBean, HttpStatus.OK);
		
		return response;
		
		
	}

	
	private String encodePasswordInRegBean(RegistrationRequest reqBean) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(reqBean.getPassword());
	}
}
