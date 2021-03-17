package com.example.demo.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.example.demo.user.service.UserService;

public class CustomLogoutHandler implements LogoutHandler {

	@Autowired
	UserService userService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
					   Authentication authentication) {
		SecurityContextHolder.clearContext();

		try {
			System.out.println("Logged Out Handler");
			response.sendRedirect("/perform-logout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
