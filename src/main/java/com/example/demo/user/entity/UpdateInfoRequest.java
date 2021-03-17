package com.example.demo.user.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateInfoRequest {
	
	@NotBlank
	@Size(min = 3, max = 20)
	private String firstName;

	@NotBlank
	@Size(max = 30)
	private String lastName;
	
	@NotBlank
	private String mobile;

}
