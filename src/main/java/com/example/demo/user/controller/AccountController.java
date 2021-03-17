package com.example.demo.user.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoService;
import com.example.demo.user.entity.AddressCreateRequest;
import com.example.demo.user.entity.ChangePassRequest;
import com.example.demo.user.entity.CreditCard;
import com.example.demo.user.entity.UpdateInfoRequest;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserServiceImpl;

@Controller
public class AccountController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	SiteInfoService siService;

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/profile")
	public String goToProfilePage(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		return "korisnik/profile";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/editprofile")
	public String goToEditInfoPage(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		return "korisnik/editprofile";

	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/editprofile")
	public String editedInfo(@Valid @ModelAttribute("request") UpdateInfoRequest request, BindingResult result,
			Principal principal) {

		if (result.hasErrors()) {
			return "/editprofile";
		}

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setMobile(request.getMobile());

		userRepo.save(user);

		return "redirect:/profile";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/changepassword")
	public String changePassPage(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		model.addAttribute("request", new ChangePassRequest());

		return "korisnik/changepass";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/changepass")
	public String editedInfo(@Valid @ModelAttribute("request") ChangePassRequest request, BindingResult result,
			Principal principal, Model model) {
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		return userService.changePass(request, user, model, result);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/editAddress")
	public String goToAddressPage(Model model, Principal principal) {
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		model.addAttribute("request", new AddressCreateRequest());

		return "korisnik/enteraddress";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/editAddress")
	public String addressPagePost(@Valid @ModelAttribute("request") AddressCreateRequest request, BindingResult result,
			Principal principal, Model model) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		return userService.enterAddress(request, result, user);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/deleteaddress")
	public String deleteAddress(Principal principal) {

		return userService.deleteAddress(principal);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/creditcard")
	public String showCCPage(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		model.addAttribute("request", new CreditCard());

		return "korisnik/creditcard";

	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/creditcarddetails")
	public String createCCDetails(@Valid @ModelAttribute("request") CreditCard request, BindingResult result,
			Principal principal, Model model) {
		
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		return userService.setCard(request, result, user, model);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/deletecc")
	public String deleteCreditCard(Principal principal) {

		return userService.deleteCreditCard(principal);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/creditcardinfo")
	public String showCCPageInfo(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		return "korisnik/creditcardinfo";

	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/uploadimage")
	public String uploadImage(Principal principal, Model model, @ModelAttribute("file") MultipartFile file) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		return userService.setProfilePhoto(user, file);

	}

}
