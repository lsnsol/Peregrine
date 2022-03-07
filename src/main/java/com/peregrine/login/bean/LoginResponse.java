package com.peregrine.login.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.commons.bean.SvcMessage;
import com.peregrine.register.bean.PeregrineUser;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class LoginResponse {
	
	private	List<SvcMessage> messages;
	Boolean usernameExists;
	private String username;
	private PeregrineUser profile;

}
