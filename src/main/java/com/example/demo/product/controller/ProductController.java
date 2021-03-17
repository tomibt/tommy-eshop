package com.example.demo.product.controller;

import java.util.ArrayList;
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

import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Brand;
import com.example.demo.product.entity.Category;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.SpecialOffer;
import com.example.demo.product.entity.Stock;
import com.example.demo.product.repository.BrandRepository;
import com.example.demo.product.repository.CategoryRepository;
import com.example.demo.product.service.ProductService;
import com.example.demo.product.service.SpecialOfferService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	SpecialOfferService soService;
	
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/productmanagment")
	public String goToProductManagment(Model model) {

		model.addAttribute("activePage", "productManagment");
		return findPaginatedProducts(1, "name", "asc", model);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/productmanagment/page/{pageNo}")
	public String findPaginatedProducts(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Product> products = page.getContent();
		
		

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("products", products);

		model.addAttribute("activePage", "productManagment");

		return "gazda/productmanagment";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/addproduct")
	public String showAddProductForm(Model model) {

		List<Brand> brandList = brandRepository.findAll();

		List<Category> categoryList = categoryRepository.findAll();

		model.addAttribute("product", new Product());
		model.addAttribute("brandList", brandList);
		model.addAttribute("categoryList", categoryList);

		model.addAttribute("stock", new StockDTO());
		model.addAttribute("activePage", "productManagment");

		return "gazda/addproduct";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/addproduct")
	public String addProductForm(@Valid @ModelAttribute("product") Product product,BindingResult result, @Valid @ModelAttribute("stock") StockDTO stock,
			BindingResult result2, MultipartFile file, Model model) {

		if (result.hasErrors() || result2.hasErrors()) {
			List<Brand> brandList = brandRepository.findAll();

			List<Category> categoryList = categoryRepository.findAll();
			model.addAttribute("brandList", brandList);
			model.addAttribute("categoryList", categoryList);
			return "gazda/addproduct";
		}

		productService.create(product, stock, file);

		return "redirect:/productmanagment";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		productService.delete(id);

		return "redirect:/productmanagment?successDelete";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/editProduct/{id}")
	public String editProductForm(@PathVariable("id") Long id, Model model) {

		Product product = productService.getProductById(id).get();
		List<Brand> brandList = brandRepository.findAll();

		List<Category> categoryList = categoryRepository.findAll();
		
		Stock stockdto = product.getStock();

		model.addAttribute("product", product);
		model.addAttribute("stockdto", stockdto);
		model.addAttribute("brandList", brandList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("activePage", "productManagment");

		return "gazda/editproduct";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/updateProduct/{id}")
	public String editProduct(@Valid @PathVariable("id") Long id, Model model,
			@ModelAttribute("product") Product product,BindingResult result ,  @Valid @ModelAttribute("stockdto") Stock stockdto,
			BindingResult result2, @ModelAttribute("file") MultipartFile file) {
		if (result.hasErrors() || result2.hasErrors()) {
			return "gazda/editproduct";
		}

		productService.update(id, product, stockdto, file);

		return "redirect:/productmanagment?successEdit";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchProducts")
	public String searchProducts(Model model, @RequestParam(value = "search", defaultValue = "") String search) {

		model.addAttribute("activePage", "productManagment");
		return findPaginatedNameSearch(search, 1, "name", "asc", model);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchProducts/page/{pageNo}")
	public String findPaginatedNameSearch(String search, @PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Product> page = productService.findByNamePaginated(pageNo, pageSize, sortField, sortDir, search);
		List<Product> products = new ArrayList<>();

		products = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("products", products);
		model.addAttribute("search", search);

		model.addAttribute("activePage", "productManagment");

		return "gazda/productsearch";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/specialOfferForm/{id}")
	public String specialOfferForm(@PathVariable("id") Long id, Model model) {
		
		Product product = productService.getProductById(id).get();
		
		model.addAttribute("product", product);
		
		model.addAttribute("specialOffer", new SpecialOffer());
		model.addAttribute("activePage", "productManagment");
		
		return "gazda/specialoffer";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/makespecialoffer/{id}")
	public String makeSpecialOffer(@PathVariable("id") Long id, @ModelAttribute("offer") SpecialOffer offer ) {
		soService.createUpdate(id, offer);
		
		return "redirect:/productmanagment";
	}
	
	

}
