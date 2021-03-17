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

import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.Stock;
import com.example.demo.product.service.ProductService;
import com.example.demo.product.service.StockService;

@Controller
public class StockController {

	@Autowired
	ProductService productService;

	@Autowired
	StockService stockService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/stockmanagment")
	public String goToStockPage(Model model) {
		model.addAttribute("activePage", "productManagment");

		return findPaginatedProductsForStock(1, "stock.quantity", "asc", model);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/stockmanagment/page/{pageNo}")
	public String findPaginatedProductsForStock(@PathVariable(value = "pageNo") int pageNo,
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

		return "gazda/stockmanagment";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchProductsForStock")
	public String searchProductsForStock(Model model,
			@RequestParam(value = "search", defaultValue = "") String search) {

		model.addAttribute("activePage", "productManagment");
		return findPaginatedNameSearchForStock(search, 1, "stock.quantity", "desc", model);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchProductsForStock/page/{pageNo}")
	public String findPaginatedNameSearchForStock(String search, @PathVariable(value = "pageNo") int pageNo,
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

		return "gazda/stocksearch";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/addStock/{id}")
	public String addStockQtyForm(@PathVariable("id") Long id, Model model) {

		Product product = productService.getProductById(id).get();

		Stock stock = product.getStock();

		model.addAttribute("stock", stock);
		model.addAttribute("stockdto", new StockDTO());
		model.addAttribute("product", product);
		model.addAttribute("activePage", "productManagment");

		return "gazda/addstockqty";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/updateStockAddQuantity/{id}")
	public String addStockQty(@Valid @PathVariable("id") Long id, @ModelAttribute("stock") StockDTO stock,
			BindingResult result) {

		if (result.hasErrors()) {
			return "gazda/addstockqty";
		}

		stockService.updateAdd(id, stock);

		return "redirect:/stockmanagment";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/substractStock/{id}")
	public String substractStockQtyForm(@PathVariable("id") Long id, Model model) {

		Product product = productService.getProductById(id).get();

		Stock stock = product.getStock();

		model.addAttribute("stock", stock);
		model.addAttribute("stockdto", new StockDTO());
		model.addAttribute("product", product);
		model.addAttribute("activePage", "productManagment");

		return "gazda/substractstockqty";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/updateStockSubstractQuantity/{id}")
	public String substractStockQty(@Valid @PathVariable("id") Long id, @ModelAttribute("stock") StockDTO stock,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "gazda/addstockqty";
		}

		return stockService.updateSubstract(id, stock);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/removeStock/{id}")
	public String removeQuantity(@PathVariable("id") Long id) {
		stockService.remove(id);
		return "redirect:/stockmanagment";
	}

}
