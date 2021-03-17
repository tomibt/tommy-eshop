package com.example.demo.product.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

	Optional<Brand> findByNameIgnoreCase(String name);
	
	boolean existsByNameIgnoreCase(String name);

	Page<Brand> findByNameLikeIgnoreCase(String string, Pageable pageable);

}
