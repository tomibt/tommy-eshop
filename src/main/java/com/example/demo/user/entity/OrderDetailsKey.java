package com.example.demo.user.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDetailsKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long orderId;
	
	private Long productId;
}
