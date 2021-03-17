package com.example.demo.product.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.product.dto.CategoryDTO;
import com.example.demo.product.entity.Category;
import com.example.demo.product.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public Optional<Category> getCategoryById(long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public Category create(CategoryDTO categorydto) {
		Category category = new Category();
		
		category.setName(categorydto.getName());
		
		return categoryRepository.save(category);
	}

	@Override
	public Category update(Long id, Category categorydto) {
		Category category = getCategoryById(id).get();
		category.setName(categorydto.getName());
		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(long id) {
		categoryRepository.deleteById(id);	
	}

	@Override
	public Page<Category> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return categoryRepository.findAll(pageable);
	}

	@Override
	public Page<Category> findByNameSearch(int pageNo, int pageSize, String sortField, String sortDirection,
			String name) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		if (name != null) {
			return categoryRepository.findByNameLike("%" + name + "%", pageable);

		}

		return categoryRepository.findAll(pageable);
	}

	@Override
	public Optional<Category> getCategoryByName(String name) {
		return categoryRepository.findByNameIgnoreCase(name);
	}

}
