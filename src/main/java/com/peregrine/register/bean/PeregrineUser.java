package com.peregrine.register.bean;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "useraccount")
@Getter @Setter
public class PeregrineUser {

	@Id
	private String loginId;
	@Field("fname")
	private String firstName;
	@Field("lname")
	private String lastName;
	private String email;
	private String password;
	@Field("phone_number")
	private String contactNumber;
	Date createDttm;
	Date updateDttm;
	private String username;
	private String avatar;
}
