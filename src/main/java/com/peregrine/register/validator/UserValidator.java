package com.peregrine.register.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.peregrine.constant.MessageConstants;
import com.peregrine.login.bean.LoginRequest;
import com.peregrine.register.bean.RegistrationRequest;

@Service
public class UserValidator {
	
	@Autowired
	Validator validator;
	
	public Set<String> validateRegFields(RegistrationRequest reqBean) {
		
		Set<String> errorCodes = new HashSet<>();
		Set<ConstraintViolation<RegistrationRequest>> constraintViolations = null;
		
		try {
			//validate the request bean values
			constraintViolations = validator.validate(reqBean);
			
			//add the error codes if validation fails
			if(!CollectionUtils.isEmpty(constraintViolations)) {
				errorCodes = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
			}
		} catch (Exception e) {
			errorCodes.add(MessageConstants.SVC_UNAVAILABLE);
		}
		return errorCodes;
	}
	
	public Set<String> validateLoginFields(LoginRequest loginBean) {
		
		Set<String> errorCodes = new HashSet<>();
		Set<ConstraintViolation<LoginRequest>> constraintViolations = null;
		
		try {
			//validate the request bean values
			constraintViolations = validator.validate(loginBean);
			
			//add the error codes if validation fails
			if(!CollectionUtils.isEmpty(constraintViolations)) {
				errorCodes = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
			}
		} catch (Exception e) {
			errorCodes.add(MessageConstants.SVC_UNAVAILABLE);
		}
		return errorCodes;
	}
}
