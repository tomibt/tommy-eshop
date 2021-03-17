package com.example.demo.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.SpecialOffer;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, Long> {

	
	
}
