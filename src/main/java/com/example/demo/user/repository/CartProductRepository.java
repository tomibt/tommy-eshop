package com.example.demo.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.Product;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.User;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long>{

	List<CartProduct> findByUser(User user);
	
	CartProduct findByProduct(Product product);

}
