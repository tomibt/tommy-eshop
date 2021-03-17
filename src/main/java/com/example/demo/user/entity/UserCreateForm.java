package com.example.demo.user.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateForm {

	@NotBlank
	@Size(min = 3, max = 20)
	private String firstName;

	@NotBlank
	@Size(max = 30)
	private String lastName;

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Email
	private String email;

	@ValidPassword
	private String password;

	@ValidPassword
	private String passwordRepeat;

	@Override
	public String toString() {
		return "UserCreateForm [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + ", passwordRepeat=" + passwordRepeat + "]";
	}

}
