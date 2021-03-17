package com.example.demo.user.service;

import java.util.Optional;

import com.example.demo.user.entity.User;
import com.example.demo.user.entity.WishList;

public interface WishListService {
	
	Optional<WishList> getWishListById(Long id);
	
	Optional<WishList> getWishListByUser(User user);

	WishList addItem(User user, Long id);
	
	void deleteById(Long id, User user);
	
	boolean checkIfUserHasProductInWishList(Long id, User user);

}
