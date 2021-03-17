package com.example.demo.user.service;
@SuppressWarnings("serial")

public class EmailExistsException extends RuntimeException{
	
	public EmailExistsException(String message) {
		super(message);
	}

}
