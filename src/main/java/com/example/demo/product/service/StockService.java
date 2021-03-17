package com.example.demo.product.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Stock;
import com.example.demo.user.entity.User;

public interface StockService {

	Optional<Stock> getStockById(long id);

	Stock create(StockDTO stockdto);

	Stock updateAdd(long id, StockDTO stockdto);

	String updateSubstract(long id, StockDTO stockdto);

	Stock remove(long id);
	void substractStockQuantityAfterOrder(User user);
	
	List<Stock> getTop5OutOfStock();
	List<Stock> getTop4OutOfStock();
	List<Stock> getTop3OutOfStock();
	List<Stock> getTop2OutOfStock();
	List<Stock> getTop1OutOfStock();
	List<Stock> getTopStockForAdminPage();
}
