package com.example.demo.product.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	
	Optional<Category> findByNameIgnoreCase(String name);

	Page<Category> findByNameLike(String string, Pageable pageable);
	
	boolean existsByNameIgnoreCase(String name);
	
}
