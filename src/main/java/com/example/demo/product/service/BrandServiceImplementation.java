package com.example.demo.product.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.entity.Brand;
import com.example.demo.product.repository.BrandRepository;

@Service
@Transactional
public class BrandServiceImplementation implements BrandService {

	@Autowired
	BrandRepository brandRepository;

	@Override
	public Optional<Brand> getBrandById(long id) {
		return brandRepository.findById(id);
	}

	@Override
	public Optional<Brand> getBrandByName(String name) {
		return brandRepository.findByNameIgnoreCase(name);
	}

	@Override
	public Brand create(Brand branddto, MultipartFile file) {
		

		Brand brand = new Brand();

		brand.setName(branddto.getName());
		
		if(file.isEmpty() || file == null) {
			brand.setLogo(null);
			return brandRepository.save(brand);
		} else {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.contains("..")) {
			System.out.println("not a valid file");
		}
		try {
			brand.setLogo(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return brandRepository.save(brand);
		}
	}

	@Override
	public Brand update(Long id,Brand brand, MultipartFile file) {

		Brand oldBrand = getBrandById(id).get();

		oldBrand.setName(brand.getName());
		
		
		
		if(file.isEmpty()) {
			return brandRepository.save(oldBrand);
		} else {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.contains("..")) {
			System.out.println("not a valid file");
		}
		try {
			oldBrand.setLogo(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return brandRepository.save(oldBrand);
		}

	}

	@Override
	public void deleteBrand(long id) {
		brandRepository.deleteById(id);

	}

	@Override
	public Page<Brand> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return brandRepository.findAll(pageable);
	}

	@Override
	public Page<Brand> findByNameSearch(int pageNo, int pageSize, String sortField, String sortDirection, String name) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		if (name != null) {
			return brandRepository.findByNameLikeIgnoreCase("%" + name + "%", pageable);

		}

		return brandRepository.findAll(pageable);
	}

}
