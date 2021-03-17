package com.example.demo.user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.WishList;
import com.example.demo.user.service.UserService;
import com.example.demo.user.service.WishListService;

@Controller
public class WishListController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SiteInfoRepository siRepo;
	
	@Autowired
	WishListService wishListService;
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/wishlist")
	public String goToWishListPage(Model model, Principal principal) {
		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siRepo.findById(1L).get();
		
		List<WishList> wishList = user.getWishList();
		
		model.addAttribute("wishList", wishList);
		model.addAttribute("si", si);
		
		return "korisnik/wishlist";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/addToWishList/{id}")
	public String addToWishList(@PathVariable("id") Long id, Principal principal) {
		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		wishListService.addItem(user, id);
		
		return "redirect:/allproducts/product/" + id + "?wishlistAdded";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/removeFromWishList/{id}")
	public String removeFromWishList(@PathVariable("id") Long id, Principal principal) {
		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		wishListService.deleteById(id, user);
		
		return "redirect:/allproducts/product/" + id + "?wishlistRemoved";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/removeFromWishListProfile/{id}")
	public String removeFromWishListProfile(@PathVariable("id") Long id, Principal principal) {
		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		wishListService.deleteById(id, user);
		
		return "redirect:/wishlist";
	}

}
