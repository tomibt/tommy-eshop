package com.example.demo.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	
	Page<Product> findByNameLikeIgnoreCase(String name, Pageable pageable);
	
	List<Product> findTop3ByOrderByDateDesc();
	
	List<Product> findTop8ByOrderByDateDesc();

	Product findTop1ByOrderByDateDesc();
	
	Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
	Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
	
}
