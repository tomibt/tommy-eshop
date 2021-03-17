package com.example.demo.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Must enter name for brand.")
	@Size(max = 15, message = "Maximum 15 letters.")
	private String name;
	
	@Lob
	@Column
	private String logo;

	public Brand(
			@NotBlank(message = "Must enter name for brand.") @Size(max = 15, message = "Maximum 15 letters.") String name) {
		super();
		this.name = name;
	}
	
	
	
	
	
}
