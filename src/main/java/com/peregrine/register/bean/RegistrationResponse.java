package com.peregrine.register.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.commons.bean.SvcMessage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class RegistrationResponse {
	
	private String username;
	private	List<SvcMessage> messages;
	Boolean usernameExists;
	private String email;
	Boolean emailExists;
	
}
