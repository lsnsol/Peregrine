package com.peregrine.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peregrine.login.bean.LoginResponse;
import com.peregrine.register.bean.RegistrationResponse;
import com.peregrine.service.ServiceHelper;

@Service
public class CommonUtils {

	@Autowired
	ServiceHelper serviceHelper;
	
	public void handleErrorMessageInRegService(Set<String> errorCodes, RegistrationResponse respBean) {
		
		respBean.setMessages(serviceHelper.populateMessages(errorCodes));
	}
	
	public void handleErrorMessageInLoginService(Set<String> errorCodes, LoginResponse respBean) {
		
		respBean.setMessages(serviceHelper.populateMessages(errorCodes));
	}
}
