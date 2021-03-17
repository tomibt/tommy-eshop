package com.example.demo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.User;
import com.example.demo.user.entity.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long>{

	Optional<WishList> findByUser(User user);

}
