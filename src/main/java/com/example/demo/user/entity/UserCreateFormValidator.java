package com.example.demo.user.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.user.service.UserService;

@Component("beforeCreateUserCreateFormValidator")
public class UserCreateFormValidator implements Validator {

	private UserService userService;

    @Autowired
    public UserCreateFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        validatePasswords(errors, form);
        validateEmail(errors, form);
        validateUsername(errors, form);
    }

    private void validatePasswords(Errors errors, UserCreateForm form) {
        if (!form.getPassword().equals(form.getPasswordRepeat())) {
            errors.reject("passerror", "Passwords do not match");
        }
    }

    private void validateEmail(Errors errors, UserCreateForm form) {
        if (userService.getUserByEmail(form.getEmail()).isPresent()) {
            errors.reject("emailExist", "User with this email already exists");
        }
    }
    
    private void validateUsername(Errors errors, UserCreateForm form) {
    	User userNull = userService.getUserByUsername(form.getUsername()).get();
        if (userNull != null) {
            errors.reject("userExist", "User with this email already exists");
        }
    }

}
