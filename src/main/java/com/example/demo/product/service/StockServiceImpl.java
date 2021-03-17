package com.example.demo.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.Stock;
import com.example.demo.product.repository.StockRepository;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.CartProductService;

@Service
@Transactional
public class StockServiceImpl implements StockService {

	@Autowired
	StockRepository stockRepository;

	@Autowired
	ProductService productService;
	
	@Autowired
	CartProductService cartService;

	@Override
	public Optional<Stock> getStockById(long id) {
		return stockRepository.findById(id);
	}

	@Override
	public Stock create(StockDTO stockdto) {
		Stock stock = new Stock();

		stock.setQuantity(stockdto.getQuantity());

		LocalDateTime date = LocalDateTime.now();

		stock.setDate(date);

		return stockRepository.save(stock);
	}

	@Override
	public Stock updateAdd(long id, StockDTO stockdto) {
		Product product = productService.getProductById(id).get();

		Stock stock = product.getStock();

		int sum = stock.getQuantity() + stockdto.getQuantity();

		stock.setQuantity(sum);

		LocalDateTime date = LocalDateTime.now();

		stock.setDate(date);

		return stockRepository.save(stock);
	}

	
	@Override
	public String updateSubstract(long id, StockDTO stockdto) {
		Product product = productService.getProductById(id).get();

		Stock stock = product.getStock();

		int sum = 0;

		if (stock.getQuantity() > stockdto.getQuantity()) {

			sum = stock.getQuantity() - stockdto.getQuantity();
			stock.setQuantity(sum);

			LocalDateTime date = LocalDateTime.now();

			stock.setDate(date);

			stockRepository.save(stock);
			
			return "redirect:/stockmanagment";
		} else {
			return "redirect:/substractStock/{id}?substractError";
	}
}

	@Override
	public Stock remove(long id) {
		Product product = productService.getProductById(id).get();

		Stock stock = product.getStock();

		int sum = 0;

		stock.setQuantity(sum);

		LocalDateTime date = LocalDateTime.now();

		stock.setDate(date);

		return stockRepository.save(stock);
	}
	
	@Override
	public void substractStockQuantityAfterOrder(User user) {
		List<CartProduct> cartList = cartService.getCartByUser(user);
		
		for (CartProduct list : cartList) {
			Product product = list.getProduct();
			
			Stock stock = product.getStock();
			
			int currentStock = stock.getQuantity();
			
			int orderQuantity= list.getQuantity();
			
			currentStock -= orderQuantity;
			
			stock.setQuantity(currentStock);
			stockRepository.save(stock);
		}
	}

	@Override
	public List<Stock> getTop5OutOfStock() {
		return stockRepository.findTop5ByOrderByQuantityAsc();
	}

	@Override
	public List<Stock> getTop4OutOfStock() {
		return stockRepository.findTop4ByOrderByQuantityAsc();
	}

	@Override
	public List<Stock> getTop3OutOfStock() {
		return stockRepository.findTop3ByOrderByQuantityAsc();
	}

	@Override
	public List<Stock> getTop2OutOfStock() {
		return stockRepository.findTop2ByOrderByQuantityAsc();
	}

	@Override
	public List<Stock> getTop1OutOfStock() {
		return stockRepository.findTop1ByOrderByQuantityAsc();
	}

	@Override
	public List<Stock> getTopStockForAdminPage() {
		List<Stock> stocks = stockRepository.findAll();
		
		List<Stock> topStock = new ArrayList<>();
		
		int i = stocks.size();
		
		if(i >= 5) {
			topStock = getTop5OutOfStock();
			return topStock;
		} else {
			switch (i) {
			case 1:
				topStock = getTop1OutOfStock();
				break;
			case 2:
				topStock = getTop2OutOfStock();
				break;
			case 3:
				topStock = getTop3OutOfStock();
				break;
			case 4:
				topStock = getTop4OutOfStock();
				break;

			default:
				break;
			}
			return topStock;
		}
	}

}
