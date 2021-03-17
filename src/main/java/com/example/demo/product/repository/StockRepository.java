package com.example.demo.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.product.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
	
	List<Stock> findTop5ByOrderByQuantityAsc();
	List<Stock> findTop4ByOrderByQuantityAsc();
	List<Stock> findTop3ByOrderByQuantityAsc();
	List<Stock> findTop2ByOrderByQuantityAsc();
	List<Stock> findTop1ByOrderByQuantityAsc();
}
