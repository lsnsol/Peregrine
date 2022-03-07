package com.peregrine.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.peregrine.login.bean.LoginRequest;
import com.peregrine.register.bean.RegistrationRequest;
import com.peregrine.security.jwt.JwtInMemoryUserDetailsService;
import com.peregrine.security.jwt.resource.JwtAuthenticationRestController;
import com.peregrine.security.jwt.resource.JwtTokenRequest;
import com.peregrine.security.jwt.resource.JwtTokenResponse;

@Service
public class CookieUtils {
	
	@Autowired
	JwtAuthenticationRestController jwtController;
	
	@Autowired
	JwtInMemoryUserDetailsService jwtInMemoryUserDetailsService;

	public HttpHeaders setCookiesPostAuthentication(Object reqBean) {
		HttpHeaders updatedHeaders = new HttpHeaders();
		try {
			addJwtCookie(reqBean, updatedHeaders);
		} catch (Exception e) {
			//add jwt creation error log
			LoggingUtils.logWarn("Invalid Credentials");
		} 
		return updatedHeaders;
	}
	
	public void addJwtCookie(Object reqBean, HttpHeaders updatedHeaders) {
		
		String jwtToken = generateJwtToken(reqBean);
		updatedHeaders.add("Authorization", "Bearer "+jwtToken);
		Date currentDate = new Date();
		Date expiryDate = new Date(currentDate.getTime() + 86400 * 1000);
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("IST"));
		updatedHeaders.add("Set-cookie", "jwt="+jwtToken+"; path=/; domain=localhost;"+"Expires="+df.format(expiryDate)+"; Max-Age=86400000;");
		
	}
	
	public String generateJwtToken(Object reqBean) {
		
		String username = null;
		String password = null;
		
		if(reqBean instanceof RegistrationRequest) {
			RegistrationRequest requestBean = (RegistrationRequest) reqBean;
			username = requestBean.getUsername();
			password = requestBean.getPassword();
		} else if(reqBean instanceof LoginRequest) {
			LoginRequest requestBean = (LoginRequest) reqBean;
			username = requestBean.getUsername();
			password = requestBean.getPassword();
		}
		JwtTokenRequest authenticationRequest = new JwtTokenRequest();
		authenticationRequest.setUsername(username);
		authenticationRequest.setPassword(password);
		ResponseEntity<?> jwtResponse = jwtController.createAuthenticationToken(authenticationRequest);
		JwtTokenResponse response = (JwtTokenResponse) jwtResponse.getBody();
		return response.getToken();
	}
}
