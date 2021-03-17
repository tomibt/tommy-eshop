package com.example.demo.product.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.SpecialOffer;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.SpecialOfferRepository;

@Service
@Transactional
public class SpecialOfferImpl implements SpecialOfferService{
	
	@Autowired
	SpecialOfferRepository soRepository;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public Optional<SpecialOffer> getOfferById(Long id) {
		return soRepository.findById(id);
	}

	@Override
	public SpecialOffer createUpdate(Long id,SpecialOffer offer) {
		List<SpecialOffer> checkOffer = soRepository.findAll();
		Product product = productService.getProductById(id).get();
		
		if(checkOffer.isEmpty()) {
			SpecialOffer newOffer = new SpecialOffer();
			newOffer.setProduct(product);
			
			if(offer.getSpecialDescription().isEmpty() || offer.getSpecialDescription().isBlank()) {
				newOffer.setSpecialDescription(null);
			} else {
				
				newOffer.setSpecialDescription(offer.getSpecialDescription());
			}
			
			product.setSpecialOffer(true);
			return soRepository.save(newOffer);
		} else {
			SpecialOffer existingOffer = getOfferById(1L).get();
			Product oldOffer = existingOffer.getProduct();
			oldOffer.setSpecialOffer(false);
			productRepository.save(oldOffer);
			existingOffer.setProduct(product);
			
			if(offer.getSpecialDescription().isEmpty() || offer.getSpecialDescription().isBlank()) {
				existingOffer.setSpecialDescription(null);
			} else {
				
				existingOffer.setSpecialDescription(offer.getSpecialDescription());
			}
			
			product.setSpecialOffer(true);
			productRepository.save(product);
			return soRepository.save(existingOffer);
		}
	}

	@Override
	public SpecialOffer getOfferByIdNonOptional(Long id) {
		return soRepository.findById(id).get();
	}

	@Override
	public List<SpecialOffer> getAllOffers() {
		return soRepository.findAll();
	}

}
