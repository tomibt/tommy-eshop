package com.example.demo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.OrderAddress;

@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long>{

}
