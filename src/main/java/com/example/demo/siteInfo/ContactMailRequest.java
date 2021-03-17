package com.example.demo.siteInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactMailRequest {
	
	@NotBlank
	private String fullname;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 50)
	private String subject;
	
	@Size(max = 250)
	@NotBlank
	private String content;

}
