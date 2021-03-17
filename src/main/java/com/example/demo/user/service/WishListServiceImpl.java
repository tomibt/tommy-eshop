package com.example.demo.user.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.WishList;
import com.example.demo.user.repository.WishListRepository;

@Service
@Transactional
public class WishListServiceImpl implements WishListService {

	@Autowired
	WishListRepository wishListRepository;
	
	@Autowired
	ProductService productService;

	@Override
	public WishList addItem(User user, Long id) {
		Product product = productService.getProductById(id).get();
		
		WishList newWishList = new WishList();
		
		newWishList.setProduct(product);
		newWishList.setUser(user);
		return wishListRepository.save(newWishList);
		
	}

	@Override
	public Optional<WishList> getWishListById(Long id) {
		return wishListRepository.findById(id);
	}

	@Override
	public void deleteById(Long id, User user) {
		List<WishList> wishList = user.getWishList();
		
		Product product = productService.getProductById(id).get();
		
		Long wishListId = null;
		for (WishList list : wishList) {
			if(list.getProduct() == product) {
				wishListId = list.getId();
			}
		}
		
		WishList matchedWishList = getWishListById(wishListId).get();
		
		matchedWishList.setProduct(null);
		matchedWishList.setUser(null);
		
		wishListRepository.save(matchedWishList);
		
		wishListRepository.deleteById(wishListId);
		
	}

	@Override
	public Optional<WishList> getWishListByUser(User user) {
		return wishListRepository.findByUser(user);
	}

	@Override
	public boolean checkIfUserHasProductInWishList(Long id, User user) {
		Product product = productService.getProductById(id).get();
		List<WishList> wishList = user.getWishList();
		
		boolean checking = false;
		
		for (WishList list : wishList) {
			if(list.getProduct() == product) {
				checking = true;
			}
		}
		
		return checking;
	}
	
}
