package com.peregrine.login.bean;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.constant.MessageConstants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class LoginRequest {

	@NotEmpty(message=MessageConstants.EMPTY_UNAME)
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", message=MessageConstants.INVALID_UNAME)
	private String username;
	
	@NotEmpty(message=MessageConstants.EMPTY_PASSWORD)
	private String password;
}
