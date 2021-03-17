package com.example.demo.product.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.user.entity.WishList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank(message = "You must write down description for this product.")
	private String description;
	
	@NotNull
	private int price;
	
	@Lob
	@Column
	private String image;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(referencedColumnName = "id")
	private Stock stock;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	@NotNull(message = "You must choose category")
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	@NotNull(message = "You must choose brand")
	private Brand brand;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
	private List<WishList> wishList;
	
	private LocalDateTime date;
	
	boolean isSpecialOffer;

	public Product(String name, String description, int price, Optional<Category> category,
			Optional<Brand> brand, LocalDateTime date, boolean isSpecialOffer) {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
