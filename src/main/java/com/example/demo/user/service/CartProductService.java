package com.example.demo.user.service;

import java.util.List;

import com.example.demo.product.entity.Product;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.CartProductRequest;
import com.example.demo.user.entity.User;

public interface CartProductService {
	
	List<CartProduct> getCartByUser(User user);
	CartProduct getCartByProduct(Product product);
	CartProduct getCartById(Long id);
	
	CartProduct saveUpdateItem(Long id, User user, CartProductRequest request);
	CartProduct updateQuantity(Long id, CartProductRequest request);
	
	int productTotalCost(List<CartProduct> list);
	
	boolean checkIfQuantityIsBiggerThanStock(Long id, User user, CartProductRequest request);
	
	void deleteOneItem(Long id);
	void deleteAll(User user);

	List<Product> getAllProductsFromShoppingCart(User user);
}
