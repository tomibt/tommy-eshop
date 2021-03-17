package com.example.demo.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.CartProductRequest;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.CartProductRepository;

@Service
@Transactional
public class CartProductServiceImpl implements CartProductService {

	@Autowired
	CartProductRepository cartRepository;

	@Autowired
	ProductService productService;

	@Override
	public List<CartProduct> getCartByUser(User user) {
		return cartRepository.findByUser(user);
	}

	@Override
	public CartProduct saveUpdateItem(Long id, User user, CartProductRequest request) {

		List<CartProduct> oldCartItem = user.getCart();

		Product product = productService.getProductById(id).get();

		if (oldCartItem.isEmpty()) {
			CartProduct newCartItem = new CartProduct();
			newCartItem.setProduct(product);
			newCartItem.setQuantity(request.getQuantity());
			newCartItem.setUser(user);
			return cartRepository.save(newCartItem);

		} else {

			CartProduct currentCartItem = null;
			CartProduct newCartItem = null;
			for (CartProduct cartProduct : oldCartItem) {
				if (cartProduct.getProduct() == product) {
					currentCartItem = cartProduct;
					int currentQuantity = currentCartItem.getQuantity();
					currentCartItem.setQuantity(currentQuantity + request.getQuantity());
				} else {
					newCartItem = new CartProduct();
					newCartItem.setProduct(product);
					newCartItem.setQuantity(request.getQuantity());
					newCartItem.setUser(user);
				}

			}

			if (currentCartItem != null) {

				return cartRepository.save(currentCartItem);
			} else {
				return cartRepository.save(newCartItem);
			}
		}

	}

	@Override
	public int productTotalCost(List<CartProduct> list) {
		int sum = 0;
		for (CartProduct cartList : list) {
			sum += cartList.getTotalCost();
		}
		return sum;
	}

	@Override
	public boolean checkIfQuantityIsBiggerThanStock(Long id, User user, CartProductRequest request) {
		Product product = productService.getProductById(id).get();
		
//		CartProduct test = cartRepository.findByProduct(product);
		
		List<CartProduct> cartList = cartRepository.findAll();
		int quantityRequested = request.getQuantity();
		
//		quantityRequested += test.getQuantity();
		
		for (CartProduct list : cartList) {
			if(list.getProduct() == product) {
				quantityRequested += list.getQuantity();
			}
		}

		int currentStock = product.getStock().getQuantity();

		boolean check = false;

		if (quantityRequested > currentStock) {
			check = true;
		}

		return check;
	}

	@Override
	public CartProduct getCartByProduct(Product product) {
		return cartRepository.findByProduct(product);
	}
	
	@Override
	public void deleteOneItem(Long id) {
		cartRepository.deleteById(id);
	}
	
	@Override
	public void deleteAll(User user) {
		List<CartProduct> currentCart = user.getCart();
		cartRepository.deleteAll(currentCart);
	}

	@Override
	public CartProduct getCartById(Long id) {
		return cartRepository.findById(id).get();
	}

	@Override
	public CartProduct updateQuantity(Long id, CartProductRequest request) {
		CartProduct currentItem = getCartById(id);
		currentItem.setQuantity(request.getQuantity());
		return cartRepository.save(currentItem);
	}

	@Override
	public List<Product> getAllProductsFromShoppingCart(User user) {
		List<CartProduct> currentCart = user.getCart();
		
		List<Product> productList = new ArrayList<>();
		
		for (CartProduct list : currentCart) {
			Product product = list.getProduct();
			productList.add(product);
		}
		
		
		return productList;
	}

}
