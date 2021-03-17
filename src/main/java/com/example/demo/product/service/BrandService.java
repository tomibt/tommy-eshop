package com.example.demo.product.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.entity.Brand;

public interface BrandService {

	Optional<Brand> getBrandById(long id);

	Optional<Brand> getBrandByName(String name);

	Brand create(Brand branddto, MultipartFile file);

	Brand update(Long id,Brand brand, MultipartFile file);

	void deleteBrand(long id);

	Page<Brand> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	Page<Brand> findByNameSearch(int pageNo, int pageSize, String sortField, String sortDirection, String name);

}
