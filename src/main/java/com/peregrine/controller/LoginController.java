package com.peregrine.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peregrine.login.bean.LoginRequest;
import com.peregrine.login.bean.LoginResponse;
import com.peregrine.service.LoginServiceManager;

@RestController
public class LoginController {

	@Autowired
	LoginServiceManager loginServiceManager;
	
	@PostMapping("/api/v1.0/tweets/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest reqBean) {
		
		ResponseEntity<LoginResponse> response = loginServiceManager.processLogin(reqBean);
		if(response == null) {
			response = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@PutMapping("/api/v1.0/tweets/resetPassword")
	public ResponseEntity<LoginResponse> resetPassword(@RequestBody LoginRequest reqBean, HttpServletRequest request) {
		ResponseEntity<LoginResponse> response = null;
		try {
			response = loginServiceManager.changePass(reqBean, request);
		} catch(Exception e) {
			response = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}
