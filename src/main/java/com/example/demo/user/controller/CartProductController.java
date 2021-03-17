package com.example.demo.user.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoService;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.CartProductRequest;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.CartProductService;
import com.example.demo.user.service.UserService;

@Controller
public class CartProductController {
	
	@Autowired
	SiteInfoService siService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CartProductService cartService;
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/shoppingcart")
	public String goToShoppingCart(Model model, Principal principal) {
		SiteInfo si = siService.getSiteInfoById(1L).get();
		
		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		
		List<CartProduct> cartItemsList = user.getCart();
		
		int productTotalCost = cartService.productTotalCost(cartItemsList);
	
		
		
		model.addAttribute("si", si);
		model.addAttribute("cartItemsList", cartItemsList);
		model.addAttribute("productTotalCost", productTotalCost);
		model.addAttribute("request", new CartProductRequest());
		
		return "korisnik/shoppingcart";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/addToShoppingCart/{id}")
	public String addProductToShoppingCart(@PathVariable("id") Long id, @ModelAttribute("request") CartProductRequest request,
			BindingResult result, Model model, Principal principal) {
		
		if(result.hasErrors()) {
			SiteInfo si = siService.getSiteInfoById(1L).get();
			model.addAttribute("si", si);
			return "redirect:/allproducts/product/" + id;
		}
		
		String userName = principal.getName();
		
		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		if(cartService.checkIfQuantityIsBiggerThanStock(id,user, request) == true) {
			return "redirect:/allproducts/product/" + id + "?stockError";
		}
		
		cartService.saveUpdateItem(id, user, request);
		
		
		return "redirect:/allproducts/product/" + id + "?successAdded";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/deleteOneItem/{id}")
	public String deleteOneItem(@PathVariable("id") Long id) {
		cartService.deleteOneItem(id);
		
		return "redirect:/shoppingcart";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/deleteAllItems")
	public String deleteAllItems(Principal principal) {
		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		cartService.deleteAll(user);
		
		return "redirect:/shoppingcart";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/changeQuantity/{id}")
	public String updateItemQuantity(@PathVariable("id") Long id, @Valid @ModelAttribute("request") CartProductRequest request,
			BindingResult result, Principal principal, Model model) {
		if(result.hasErrors()) {
			SiteInfo si = siService.getSiteInfoById(1L).get();
			String userName = principal.getName();
			User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
			List<CartProduct> cartItemsList = user.getCart();
			int productTotalCost = cartService.productTotalCost(cartItemsList);
			model.addAttribute("si", si);
			model.addAttribute("cartItemsList", cartItemsList);
			model.addAttribute("productTotalCost", productTotalCost);
			model.addAttribute("request", new CartProductRequest());
			
			return "korisnik/shoppingcart";
		}
		
		cartService.updateQuantity(id, request);
		return "redirect:/shoppingcart";
	}

}
