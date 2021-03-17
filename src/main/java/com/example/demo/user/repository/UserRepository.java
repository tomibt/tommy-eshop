package com.example.demo.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(Long id);


	Optional<User> findByUsernameIgnoreCase(String username);

	Optional<User> findByEmailIgnoreCase(String email);

	Optional<User> findByUsernameOrEmailIgnoreCase(String username, String email);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCase(String username);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User getUserByUsername(@Param("username") String username);


	User findByEmailOrUsernameIgnoreCase(String name, String name2);
	
	List<User> findTop10ByOrderByDateDesc();
	
	Page<User> findByFirstNameOrLastNameOrEmailOrMobileOrUsernameLike(String firstName, String lastName, String email, String string,String username, Pageable pageable);
	
	List<User> findTop5ByOrderByDateDesc();
	List<User> findTop4ByOrderByDateDesc();
	List<User> findTop3ByOrderByDateDesc();
	List<User> findTop2ByOrderByDateDesc();
	List<User> findTop1ByOrderByDateDesc();
	

}
