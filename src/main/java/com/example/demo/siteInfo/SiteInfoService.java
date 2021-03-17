package com.example.demo.siteInfo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.demo.user.entity.Address;
import com.example.demo.user.repository.AddressRepository;

@Service
@Transactional
public class SiteInfoService {

	@Autowired
	SiteInfoRepository siRepo;
	
	@Autowired
	AddressRepository addressRepo;
	
	@Autowired
	JavaMailSender mailSender;
	
	public Optional<SiteInfo> getSiteInfoById(Long id){
		return siRepo.findById(id);
	}
	
	public String setSitePreferences(SiteInfoDTO request, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			SiteInfo si = siRepo.findById(1L).get();
			
			model.addAttribute("si", si);
			model.addAttribute("activePage", "siteinfo");
			return "gazda/siteinfoeditform";
		}
		
		SiteInfo si = siRepo.findById(1L).get();
		
		si.setShopName(request.getShopName());
		si.setTitleOfPage(request.getTitleOfPage());
		si.setHomeNumber(request.getHomeNumber());
		si.setMobileNumber(request.getMobileNumber());
		si.setEmail(request.getEmail());
		si.setFb(request.getFb());
		si.setTwitter(request.getTwitter());
		si.setYoutube(request.getYoutube());
		si.setIg(request.getIg());
		
		Address address = si.getAddress();
		address.setCity(request.getCity());
		address.setZipCode(request.getZipCode());
		address.setCountry(request.getCountry());
		address.setStreet(request.getStreet());
		addressRepo.save(address);
		
		siRepo.save(si);
		
		return "redirect:/siteinfo";
		
	}
	
	
	public String sendEmail(ContactMailRequest request, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/contact";
		}
		
		String fullname = request.getFullname();
		String email = request.getEmail();
		String subject = request.getSubject();
		String content = request.getContent();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(request.getEmail());
		message.setTo("tommy.ecommerce.project@gmail.com");
		
		String mailSubject = fullname + " has sent a message";
		String mailContent = "Sender Name: "+ fullname + "\n";
		
		mailContent += "Sender E-mail: " + email + "\n";
		mailContent += "Subject: " + subject + "\n";
		mailContent += "Content: " + content + "\n";
		message.setSubject(mailSubject);
		message.setText(mailContent);
		
		mailSender.send(message);
		
		return "redirect:/contact?mailSuccess";
	}
	
}
