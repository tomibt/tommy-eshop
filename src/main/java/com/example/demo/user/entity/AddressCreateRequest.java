package com.example.demo.user.entity;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddressCreateRequest {
	
	@NotBlank
	private String street;
	
	@NotBlank
	private String city;
	
	@NotBlank
	private String zipCode;
	
	@NotBlank
	private String country;

}
