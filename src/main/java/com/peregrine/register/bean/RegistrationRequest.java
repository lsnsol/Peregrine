package com.peregrine.register.bean;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.peregrine.constant.MessageConstants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class RegistrationRequest {
	
	@NotEmpty(message=MessageConstants.EMPTY_EMAIL)
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message=MessageConstants.INVALID_EMAIL)
	private String email;
	
	@NotEmpty(message=MessageConstants.EMPTY_PASSWORD)
	private String password;
	
	@NotEmpty(message=MessageConstants.EMPTY_FNAME)
	@Pattern(regexp = "^[a-zA-Z\\s\\-'.]*$", message=MessageConstants.INVALID_FNAME)
	private String firstName;
	
	@NotEmpty(message=MessageConstants.EMPTY_LNAME)
	@Pattern(regexp = "^[a-zA-Z\\s\\-'.]*$", message=MessageConstants.INVALID_LNAME)
	private String lastName;
	
	@NotEmpty(message=MessageConstants.EMPTY_UNAME)
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", message=MessageConstants.INVALID_UNAME)
	private String username;
	
	@NotEmpty(message=MessageConstants.INVALID_PHONE_NUM)
	private String contactNumber;
	
	private String avatar;
	
	private String profileId;
}
