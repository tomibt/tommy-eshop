package com.example.demo.user.entity;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ChangePassRequest {
	
	@NotBlank
	private String oldpassword;
	
	@NotBlank
	@ValidPassword
	private String newpassword;
	
	@NotBlank
	@ValidPassword
	private String newpassword2;

}
