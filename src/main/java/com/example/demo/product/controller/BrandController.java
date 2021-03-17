package com.example.demo.product.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.entity.Brand;
import com.example.demo.product.repository.BrandRepository;
import com.example.demo.product.service.BrandService;

@Controller
public class BrandController {

	@Autowired
	BrandRepository brandRepository;
	
	@Autowired
	BrandService brandService;
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/brandsmanagment")
	public String goToBrandsManagment(Model model) {
		
		model.addAttribute("activePage", "productManagment");
		return findPaginatedBrands(1, "name", "asc", model);
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/brandsmangment/page/{pageNo}")
		public String findPaginatedBrands(@PathVariable (value = "pageNo") int pageNo, 
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				Model model) {
			int pageSize = 10;
			
			Page<Brand> page = brandService.findPaginated(pageNo, pageSize, sortField, sortDir);
			List<Brand> brands = page.getContent();
			
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		
		model.addAttribute("brands", brands);
		
		model.addAttribute("activePage", "productManagment");
		
		
		
		return "gazda/brandsmanagment";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/addbrand")
	public String addBrand(Model model) {
		model.addAttribute("activePage", "productManagment");
		
		model.addAttribute("request", new Brand());
		
		return "gazda/addbrand";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/createBrand")
	public String createBrand(@Valid @ModelAttribute("request") Brand request, BindingResult result, @ModelAttribute("file") MultipartFile file, Model model) {
		if(brandRepository.existsByNameIgnoreCase(request.getName())) {
			model.addAttribute("brandNameExist", "Brand with this name already exists");
			return "gazda/addbrand";
		}
		
		if(result.hasErrors()) {
			return "gazda/addbrand";
		}
		brandService.create(request, file);
		
		return "redirect:/brandsmanagment";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/editBrand/{id}")
	public String editBrandForm(@PathVariable("id") Long id, Model model) {
		
		model.addAttribute("activePage", "productManagment");
		
		Brand brand = brandService.getBrandById(id).get();
		
		String file = brand.getLogo();
		
		model.addAttribute("brand", brand);
		
		model.addAttribute("file", file);
		
		return "gazda/editbrand";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/updateBrand/{id}")
	public String editBrand(@Valid @PathVariable("id") Long id,@ModelAttribute("brand") Brand brand, BindingResult result,
			@ModelAttribute("file") MultipartFile file) {
		
		if(result.hasErrors()) {
			return "gazda/editbrand";
		}
		
		brandService.update(id,brand, file);
		
		return "redirect:/brandsmanagment?successEdit";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/deleteBrand/{id}")
	public String deleteBrand(@PathVariable("id") Long id) {
		brandService.deleteBrand(id);
		
		return "redirect:/brandsmanagment?successDelete";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchBrands")
	public String searchBrands(Model model, @RequestParam(value = "search",defaultValue = "") String search) {
		
		model.addAttribute("activePage", "productManagment");
		return findByBrandNameSearch(search, 1, "name", "asc", model);
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchBrands/page/{pageNo}")
	public String findByBrandNameSearch(String search, @PathVariable (value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,Model model) {
		int pageSize = 10;
		
		Page<Brand> page = brandService.findByNameSearch(pageNo, pageSize, sortField, sortDir, search);
		List<Brand> brands = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		
		model.addAttribute("brands", brands);
		model.addAttribute("search", search);
		
		model.addAttribute("activePage", "productManagment");
		
		
		
		return "gazda/brandsearch";
	}
}
