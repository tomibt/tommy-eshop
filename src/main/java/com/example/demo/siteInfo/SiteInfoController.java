package com.example.demo.siteInfo;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SiteInfoController {
	
	@Autowired
	SiteInfoRepository siRepo;
	
	@Autowired
	SiteInfoService siService;
	
	@Autowired
	JavaMailSender mailSender;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/siteinfo")
	public String viewPreferencesSite(Model model) {
		
		SiteInfo si = siRepo.findById(1L).get();
		
		model.addAttribute("si", si);
		
		model.addAttribute("request", new SiteInfoDTO());
		
		model.addAttribute("activePage", "siteinfo");
		
		
		return "gazda/siteinfo";
		
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/siteinfoedit")
	public String editPreferencesSite(@Valid @ModelAttribute("request") SiteInfoDTO request, BindingResult result, Model model) {
		SiteInfo si = siRepo.findById(1L).get();
		
		model.addAttribute("si", si);
		return siService.setSitePreferences(request, result, model);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/siteinfoeditpage")
	public String viewPreferencesSiteEditPage(Model model) {
		
		SiteInfo si = siRepo.findById(1L).get();
		
		model.addAttribute("si", si);
		
		model.addAttribute("request", new SiteInfoDTO());
		
		model.addAttribute("activePage", "siteinfo");
		
		
		return "gazda/siteinfoeditform";
		
		
	}
	
	
	@PostMapping("/sendEmail")
	public String sendEmail(@Valid @ModelAttribute("request") ContactMailRequest request,  BindingResult result) {
		return siService.sendEmail(request, result);
	}

}
