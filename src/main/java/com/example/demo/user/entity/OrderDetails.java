package com.example.demo.user.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.example.demo.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
	
	@EmbeddedId
	private OrderDetailsKey id;
	
	@ManyToOne
	@MapsId("orderId")
	@JoinColumn(name = "orderId")
	Order order;
	
	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name = "productId")
	Product product;
	
	private int quantity;
	
	private int productPrice;

}
