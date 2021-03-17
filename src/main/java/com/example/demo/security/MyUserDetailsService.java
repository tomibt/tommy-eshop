package com.example.demo.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.MyUserDetails;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	User user;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Only username login
//		User user = userRepository.getUserByUsername(username);
		
		//Username Or email login
//		User user = userRepository.findByEmailOrUsername(username, username);
		
		//Username or email login with optional user
		User user = userRepository.findByUsernameOrEmailIgnoreCase(username, username).get();

		if (user == null) {
			throw new UsernameNotFoundException("You are not signUped with that email");
		}

		return new MyUserDetails(user);
//		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities());
	}

//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Set<Role> roles = user.getRoles();
//		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//		for (Role role : roles) {
//			authorities.add(new SimpleGrantedAuthority(role.getName()));
//		}
//		return authorities;
//	}

}