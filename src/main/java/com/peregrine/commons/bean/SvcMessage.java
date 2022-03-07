package com.peregrine.commons.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value=Include.NON_DEFAULT)
public class SvcMessage {
	
	private String code;
	private String message;
	private String type;
}
