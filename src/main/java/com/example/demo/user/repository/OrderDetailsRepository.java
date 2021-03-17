package com.example.demo.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.Product;
import com.example.demo.user.entity.Order;
import com.example.demo.user.entity.OrderDetails;
import com.example.demo.user.entity.OrderDetailsKey;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsKey>{
	
	List<OrderDetails> findByOrder(Order order);
	
	List<OrderDetails> findByProduct(Product product);

}
