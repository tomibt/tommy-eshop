package com.example.demo.product.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ActiveClassControllerAdvice {

	@ModelAttribute("active")
    public String cssActivePage() {
        return "active";
    }
	
}
