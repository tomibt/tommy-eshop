package com.example.demo.product.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.demo.product.dto.CategoryDTO;
import com.example.demo.product.entity.Category;

public interface CategoryService {
	
	Optional<Category> getCategoryById(long id);
	
	Optional<Category> getCategoryByName(String name);
	
	Category create(CategoryDTO categorydto);
	
	Category update(Long id, Category category);

	void deleteCategory(long id);
	
	Page<Category> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	Page<Category> findByNameSearch(int pageNo, int pageSize, String sortField, String sortDirection, String name);
}
