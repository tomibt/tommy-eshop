package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.dto.CategoryDTO;
import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Brand;
import com.example.demo.product.entity.Category;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.BrandService;
import com.example.demo.product.service.CategoryService;
import com.example.demo.product.service.ProductService;
import com.example.demo.user.entity.Address;
import com.example.demo.user.entity.UserCreateForm;
import com.example.demo.user.repository.AddressRepository;
import com.example.demo.user.service.UserService;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Component
public class AppStartupRunner implements CommandLineRunner{
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	BrandService brandService;
	
	@Override
	public void run(String... args) throws Exception {
		
		// create users
		
//		UserCreateForm form1 = new UserCreateForm("Tomislav", "Ognenovski", "gadbt", "tomi@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form11 = new UserCreateForm("Gabi", "Bucukovska", "gabirielab", "gabi@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form2 = new UserCreateForm("Dejan", "Trajkovski", "dekibt", "dekibt@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form3 = new UserCreateForm("Sinisa", "Sinkovski", "sinisa", "sinisa@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form4 = new UserCreateForm("Kire", "Blazev", "kirebt", "kirebt@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form5 = new UserCreateForm("Koco", "Strezovski", "kokibt", "kokibt@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form6 = new UserCreateForm("Cupa", "Boglev", "cupabt", "cupabt@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form7 = new UserCreateForm("Klime", "Klimevski", "klimebt", "klimebt@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form8 = new UserCreateForm("Tome", "Tomevski", "tomebt", "tomebt@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form9 = new UserCreateForm("Pande", "Pandev", "pandev", "pandev@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		UserCreateForm form10 = new UserCreateForm("Timotej", "Tesla", "tesla", "tesla@tomi.com", passwordEncoder.encode("User123@"), passwordEncoder.encode("User123@"));
//		
//		userService.create(form1);
//		userService.create(form2);
//		userService.create(form3);
//		userService.create(form4);
//		userService.create(form5);
//		userService.create(form6);
//		userService.create(form7);
//		userService.create(form8);
//		userService.create(form9);
//		userService.create(form10);
//		userService.create(form11);
		
		
		
		
		
		
	}

}
