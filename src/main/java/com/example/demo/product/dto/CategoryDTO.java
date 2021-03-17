package com.example.demo.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor	
@AllArgsConstructor
public class CategoryDTO {

	@NotBlank
	@Size(min = 3)
	private String name;
}
