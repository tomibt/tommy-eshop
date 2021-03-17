package com.example.demo.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoRepository;
import com.example.demo.user.entity.LoginRequest;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserCreateForm;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	SiteInfoRepository siRepo;

	private AuthenticationManager authenticationManager;

	@GetMapping("/register")
	public String goToSignUpPage(Model model) {

		SiteInfo si = siRepo.findById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", new UserCreateForm());
		return "register";
	}

	@PostMapping("/register")
	public String createUser(@Valid @ModelAttribute("user") UserCreateForm user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			SiteInfo si = siRepo.findById(1L).get();

			model.addAttribute("si", si);
			return "/register";
		}

		if (userRepo.existsByEmailIgnoreCase(user.getEmail())) {
			SiteInfo si = siRepo.findById(1L).get();

			model.addAttribute("si", si);
			model.addAttribute("emailExist", "Email taken.");
			return "/register";
		}

		if (userRepo.existsByUsernameIgnoreCase(user.getUsername())) {
			SiteInfo si = siRepo.findById(1L).get();

			model.addAttribute("si", si);
			model.addAttribute("userExist", "Username taken");
			return "/register";
		}

		if (!user.getPassword().equals(user.getPasswordRepeat())) {
			SiteInfo si = siRepo.findById(1L).get();

			model.addAttribute("si", si);
			model.addAttribute("passerror", "Passwords dont match");
			return "/register";
		}

		userService.create(user);
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String goToLoginPage(Model model) {
		model.addAttribute("login", new LoginRequest());
		SiteInfo si = siRepo.findById(1L).get();

		model.addAttribute("si", si);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}

		return "redirect:/";
	}

	@PostMapping("/login")
	public String signIn(@ModelAttribute("request") LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/index";

	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/manageUsers")
	public String userManagment(Model model) {
		
		model.addAttribute("activePage", "users");
		
		return findPaginated(1, "firstName", "asc", model);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 10;
		
		Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<User> users = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("users", users);
		
		model.addAttribute("activePage", "users");
		return "gazda/allusers";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
		
		return "redirect:/manageUsers";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/lastregistered")
	public String lastRegistered(Model model) {
		
		List<User> users = userRepo.findTop10ByOrderByDateDesc();
		
		model.addAttribute("users", users);
		
		model.addAttribute("activePage", "users");
		
		return "gazda/lastusers";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchUsers")
	public String searchUsers(Model model, @RequestParam(value = "search",defaultValue = "") String search) {
		
		model.addAttribute("activePage", "users");
		return findPaginatedMultipleUserSearch(search, 1, "firstName", "asc", model);
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/searchUsers/page/{pageNo}")
	public String findPaginatedMultipleUserSearch(String search, @PathVariable (value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,Model model) {
		int pageSize = 10;
		
		Page<User> page = userService.findByMultipleSearches(pageNo, pageSize,sortField, sortDir, search, search, search, search, search);
		List<User> users = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		
		model.addAttribute("users", users);
		model.addAttribute("search", search);
		
		model.addAttribute("activePage", "users");
		
		
		
		return "gazda/usersearch";
	}
	
	
}
