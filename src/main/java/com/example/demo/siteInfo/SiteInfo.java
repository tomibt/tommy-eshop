package com.example.demo.siteInfo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.example.demo.user.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfo {
	
	@Id
	private Long id;
	
	private String shopName;
	
	private String titleOfPage;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(referencedColumnName = "id")
	private Address address;
	
	private String homeNumber;
	
	private String mobileNumber;
	
	@Email
	private String email;
	
	private String fb;
	
	private String ig;
	
	private String twitter;
	
	private String youtube;
	
}
