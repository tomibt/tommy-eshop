package com.example.demo.product.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.product.entity.SpecialOffer;

public interface SpecialOfferService {

	Optional<SpecialOffer> getOfferById(Long id);
	
	List<SpecialOffer> getAllOffers();
	
	SpecialOffer getOfferByIdNonOptional(Long id);
	
	SpecialOffer createUpdate(Long id, SpecialOffer offer);
}
