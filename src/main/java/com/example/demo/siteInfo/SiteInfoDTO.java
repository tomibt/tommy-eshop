package com.example.demo.siteInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SiteInfoDTO {
	
	@NotBlank(message = "ShopName should not be blank.")
	private String shopName;
	
	@NotBlank(message = "Set title for page")
	@Size(min = 10, max = 25)
	private String titleOfPage;
	
	@NotBlank(message = "City should not be blank.")
	private String city;
	
	@NotBlank(message = "ZipCode should not be blank.")
	private String zipCode;
	
	@NotBlank(message = "Country should not be blank.")
	private String country;
	
	@NotBlank(message = "Street should not be blank.")
	private String street;
	
	@NotBlank(message = "Number should not be blank.")
	private String homeNumber;
	
	
	private String mobileNumber;
	
	@Email
	private String email;
	
	
	private String fb;
	
	private String ig;
	
	private String twitter;
	
	private String youtube;
	
	private String googleMaps;

}
