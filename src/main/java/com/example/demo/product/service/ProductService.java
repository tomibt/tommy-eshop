package com.example.demo.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.Stock;

public interface ProductService {

	Optional<Product> getProductById(long id);

	Product create(Product product, StockDTO stockdto, MultipartFile file);

	Product update(long id, Product product, Stock stockdto, MultipartFile file);

	void delete(long id);

	Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	Page<Product> findByNamePaginated(int pageNo, int pageSize, String sortField, String sortDirection,
			String name);

	Page<Product> findPaginatedSortedByPriceAsc(int pageNo, int pageSize, String sortField, String sortDirection);
	Page<Product> findPaginatedSortedByPriceDesc(int pageNo, int pageSize, String sortField, String sortDirection);
	
	boolean checkIfProductIsSetToSpecialOffer(Long id);
	
	List<Product> showLast3Entries();
	
	List<Product> showLast8Entries();
	
	Product showLastProduct();
	
	boolean isProductAvailable(Long id);
	
	Product getTopSellingProduct();
}
