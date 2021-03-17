package com.example.demo.user.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.user.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByShippedFalse();
	List<Order> findByPaymentSuccessfullFalseAndShippedTrue();
	List<Order> findByPaymentSuccessfullTrueAndShippedTrue();
	Page<Order> findByShippedFalse(Pageable pageable);
	Page<Order> findByPaymentSuccessfullFalseAndShippedTrue(Pageable pageable);
	Page<Order> findByPaymentSuccessfullTrueAndShippedTrue(Pageable pageable);
	

	List<Order> findTop5ByOrderByDateDesc();
	List<Order> findTop4ByOrderByDateDesc();
	List<Order> findTop3ByOrderByDateDesc();
	List<Order> findTop2ByOrderByDateDesc();
	List<Order> findTop1ByOrderByDateDesc();
}
