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

import com.example.demo.product.dto.CategoryDTO;
import com.example.demo.product.entity.Category;
import com.example.demo.product.repository.CategoryRepository;
import com.example.demo.product.service.CategoryService;

@Controller
public class CategoryController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CategoryService categoryService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/categorymanagment")
	public String goToCategoryManagment(Model model) {

		model.addAttribute("activePage", "productManagment");
		return findPaginatedCategory(1, "name", "asc", model);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/categorymanagment/page/{pageNo}")
	public String findPaginatedCategory(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Category> page = categoryService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Category> categorys = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("categorys", categorys);

		model.addAttribute("activePage", "productManagment");

		return "gazda/categorymanagment";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("activePage", "productManagment");

		model.addAttribute("request", new CategoryDTO());

		return "gazda/addcategory";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/addcategory")
	public String createCategory(@Valid @ModelAttribute("request") CategoryDTO request, BindingResult result,
			Model model) {
		if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
			model.addAttribute("categoryNameExist", "Category with this name already exists");
			return "gazda/addcategory";
		}

		if (result.hasErrors()) {
			return "gazda/addcategory";
		}
		categoryService.create(request);

		return "redirect:/categorymanagment";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable("id") Long id) {
		categoryService.deleteCategory(id);

		return "redirect:/categorymanagment?successDelete";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/editCategory/{id}")
	public String editCategoryForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("activePage", "productManagment");

		Category category = categoryService.getCategoryById(id).get();

		model.addAttribute("category", category);

		return "gazda/editcategory";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/updateCategory/{id}")
	public String editCategory(@Valid @PathVariable("id") Long id, @ModelAttribute("category") Category category,
			BindingResult result) {

		if (result.hasErrors()) {
			return "gazda/editcategory";
		}

		categoryService.update(id, category);

		return "redirect:/categorymanagment?successEdit";
	}

}
