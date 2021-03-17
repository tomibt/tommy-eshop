package com.example.demo.user.service;

@SuppressWarnings("serial")
public class UsernameExistException extends RuntimeException{

	UsernameExistException(String message){
		super(message);
	}
	
}
