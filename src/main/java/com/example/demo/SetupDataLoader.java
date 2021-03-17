package com.example.demo;

import java.time.LocalDateTime;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoRepository;
import com.example.demo.user.entity.Address;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.AddressRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	SiteInfoRepository siRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!userRepository.existsByEmailIgnoreCase("test@test.com")) {

			createRoleIfNotFound("ROLE_ADMIN");
			createRoleIfNotFound("ROLE_USER");

			Role adminRole = roleRepository.findByName("ROLE_ADMIN");
			User user = new User();
			user.setFirstName("Administrator");
			user.setLastName("AdminLast");
			user.setUsername("admin");
			user.setPassword(new BCryptPasswordEncoder().encode("Admin123@"));
			user.setEmail("test@test.com");

			user.setRoles(Collections.singleton(adminRole));

			LocalDateTime date = LocalDateTime.now();
			user.setDate(date);

			userRepository.save(user);
		}
		
		if(!siRepository.existsById(1L)) {
			SiteInfo si = new SiteInfo();
			
			Address address = new Address();
			
			si.setId(1L);
			si.setEmail("tommy.ecommerce.project@outlook.com");
			si.setHomeNumber("047-222-333");
			si.setMobileNumber("077-255-355");
			si.setShopName("Tommy Ecommerce dooel");
			si.setTitleOfPage("Tommy ECommerce");
			si.setFb("https://www.fb.com");
			si.setIg("https://www.instagram.com");
			si.setYoutube("https://www.youtube.com");
			si.setTwitter("https://www.twitter.com");
						
			address.setCity("Bitola");
			address.setCountry("Macedonia");
			address.setStreet("Sirok sokak BB");
			address.setZipCode("7000");
			addressRepository.save(address);
			
			si.setAddress(address);
			
			siRepository.save(si);
		}

	}

	@Transactional
	Role createRoleIfNotFound(String name) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			roleRepository.save(role);
		}
		return role;
	}

}
