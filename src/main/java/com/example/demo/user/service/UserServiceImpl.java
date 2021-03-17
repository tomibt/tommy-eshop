package com.example.demo.user.service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoService;
import com.example.demo.user.entity.Address;
import com.example.demo.user.entity.AddressCreateRequest;
import com.example.demo.user.entity.ChangePassRequest;
import com.example.demo.user.entity.CreditCard;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserCreateForm;
import com.example.demo.user.repository.AddressRepository;
import com.example.demo.user.repository.CreditCardRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	CreditCardRepository ccRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	SiteInfoService siService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public Optional<User> getUserById(long id) {

		return userRepository.findById(id);
	}

	@Override
	public Optional<User> getUserByEmail(String email) {

		return userRepository.findByEmailIgnoreCase(email);
	}

	@Override
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsernameIgnoreCase(username);
	}

	@Override
	public Collection<User> getAllUsers() {

		return userRepository.findAll();
	}

	@Override
	public User create(UserCreateForm form) {

		User user = new User();
		user.setFirstName(form.getFirstName());
		user.setLastName(form.getLastName());
		user.setUsername(form.getUsername());
		user.setEmail(form.getEmail());

		user.setPassword(passwordEncoder.encode(form.getPassword()));

		Role role = roleRepository.findByName("ROLE_USER");
		user.setRoles(Collections.singleton(role));

		LocalDateTime date = LocalDateTime.now();
		user.setDate(date);

		return userRepository.save(user);
	}

	@Override
	public User findUserByUsername(String name) {
		return userRepository.findByEmailOrUsernameIgnoreCase(name, name);
	}

//	private boolean usernameExists(String username) {
//		return userRepository.existsByUsername(username);
//	}
//
//	private boolean emailExists(final String email) {
//        return userRepository.existsByEmail(email);
//    }

	public String changePass(ChangePassRequest pass, User user, Model model, BindingResult result) {

		if (result.hasErrors()) {
			return "/changepass";
		}

		if (pass.getOldpassword().equals(user.getPassword())) {
			model.addAttribute("wrongpass", "Enter pass again");
			return "/changepass";
		}

		if (!pass.getNewpassword().equals(pass.getNewpassword2())) {
			model.addAttribute("passmatch", "Passwords didnt match");
			return "/changepass";
		}

		user.setPassword(passwordEncoder.encode(pass.getNewpassword()));

		userRepository.save(user);

		return "redirect:/profile";
	}

	public String enterAddress(AddressCreateRequest request, BindingResult result, User user) {
		if (result.hasErrors()) {
			return "korisnik/enteraddress";
		}

		if (user.getAddress() == null) {

			Address address = new Address();

			address.setCity(request.getCity());
			address.setStreet(request.getStreet());
			address.setZipCode(request.getZipCode());
			address.setCountry(request.getCountry());
			addressRepository.save(address);
			user.setAddress(address);

			userRepository.save(user);

			return "redirect:/profile";
		} else {
			Address address = user.getAddress();

			address.setCity(request.getCity());
			address.setStreet(request.getStreet());
			address.setZipCode(request.getZipCode());
			address.setCountry(request.getCountry());

			addressRepository.save(address);

			userRepository.save(user);
			return "redirect:/profile";
		}

	}
	
	
	public String deleteAddress(Principal principal) {
		String userName = principal.getName();
		
		User user = userRepository.findByEmailOrUsernameIgnoreCase(userName, userName);
		
		Address address = user.getAddress();
		
		user.setAddress(null);
		
		
		addressRepository.delete(address);
		
		userRepository.save(user);
		
		return "redirect:/profile";
		
	}

	public String setCard(CreditCard card, BindingResult result, User user, Model model) {
		if (result.hasErrors()) {
			SiteInfo si = siService.getSiteInfoById(1L).get();

			model.addAttribute("si", si);
			return "korisnik/creditcard";
		}

		if (user.getCc() == null) {
			CreditCard cc = new CreditCard();
			cc.setNumber(card.getNumber());
			cc.setName(card.getName());
			cc.setMonth(card.getMonth());
			cc.setYear(card.getYear());
			cc.setCcv(card.getCcv());
			ccRepository.save(cc);
			
			user.setCc(cc);
			userRepository.save(user);
			return "redirect:/profile";
		} else {
			CreditCard cc = user.getCc();
			cc.setNumber(card.getNumber());
			cc.setName(card.getName());
			cc.setMonth(card.getMonth());
			cc.setYear(card.getYear());
			cc.setCcv(card.getCcv());

			ccRepository.save(cc);
			return "redirect:/profile";

		}

	}

	public String deleteCreditCard(Principal principal) {

		String userName = principal.getName();

		User cUser = userRepository.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		CreditCard cc = cUser.getCc();
		
		//long ccId = cc.getId();
			
		cUser.setCc(null);
		
		userRepository.save(cUser);
		
		ccRepository.delete(cc);
		

		return "redirect:/creditcardinfo?success";
	}

	public String setProfilePhoto(User user, MultipartFile file) {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.contains("..")) {
			System.out.println("not a valid file");
		}
		try {
			user.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		userRepository.save(user);

		return "redirect:/profile";

	}

	@Override
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		
		return userRepository.findAll(pageable);
	}

	@Override
	public void deleteUserById(long id) {
		userRepository.deleteById(id);
		
	}

	@Override
	public Page<User> findByMultipleSearches(int pageNo, int pageSize,String sortField, String sortDirection,String firstName, String lastName, String email, String mobile,
			String username) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		
		if(firstName != null && lastName != null && email != null
				&& mobile != null && username != null) {
			return userRepository.findByFirstNameOrLastNameOrEmailOrMobileOrUsernameLike("%"+firstName+"%", "%"+lastName+"%", "%"+email+"%", "%"+mobile+"%", "%"+username+"%", pageable);
			
		}
		
		return userRepository.findAll(pageable);
	}

	@Override
	public Optional<User> getUserByUserNameOrEmailIgnoreCase(String username, String email) {
		return userRepository.findByUsernameOrEmailIgnoreCase(username, email);
	}

	@Override
	public List<User> getLast5UsersRegistered() {
		return userRepository.findTop5ByOrderByDateDesc();
	}

	@Override
	public List<User> getLast4UsersRegistered() {
		return userRepository.findTop4ByOrderByDateDesc();
	}

	@Override
	public List<User> getLast3UsersRegistered() {
		return userRepository.findTop3ByOrderByDateDesc();
	}

	@Override
	public List<User> getLast2UsersRegistered() {
		return userRepository.findTop2ByOrderByDateDesc();
	}

	@Override
	public List<User> getLast1UsersRegistered() {
		return userRepository.findTop1ByOrderByDateDesc();
	}

	@Override
	public List<User> getLastUsersForAdminPage() {
		List<User> users = userRepository.findAll();
		
		List<User> topUsers = new ArrayList<>();
		
		int i = users.size();
		
		if(i >= 5) {
			topUsers = getLast5UsersRegistered();
			return topUsers;
		} else {
			switch (i) {
			case 1:
				topUsers = getLast1UsersRegistered();
				break;
			case 2:
				topUsers = getLast2UsersRegistered();
				break;
			case 3:
				topUsers = getLast3UsersRegistered();
				break;
			case 4:
				topUsers = getLast4UsersRegistered();
				break;

			default:
				break;
			}
			return topUsers;
		}
	}
}
