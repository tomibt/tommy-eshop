package com.example.demo.user.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserCreateForm;

public interface UserService {

	Optional<User> getUserById(long id);

	Optional<User> getUserByEmail(String email);
	
	Optional<User> getUserByUsername(String username);
	
	Optional<User> getUserByUserNameOrEmailIgnoreCase(String username, String email);

	Collection<User> getAllUsers();

	User create(UserCreateForm form);

	User findUserByUsername(String name);
	
	void deleteUserById(long id);
	
	Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	
	Page<User> findByMultipleSearches(int pageNo, int pageSize, String sortField, String sortDirection, String firstName, String lastName, String email, String mobile,String username);
	
	List<User> getLast5UsersRegistered();
	List<User> getLast4UsersRegistered();
	List<User> getLast3UsersRegistered();
	List<User> getLast2UsersRegistered();
	List<User> getLast1UsersRegistered();
	
	List<User> getLastUsersForAdminPage();


}
