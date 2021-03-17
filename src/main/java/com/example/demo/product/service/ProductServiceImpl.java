package com.example.demo.product.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.product.dto.StockDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.SpecialOffer;
import com.example.demo.product.entity.Stock;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.repository.StockRepository;
import com.example.demo.user.entity.OrderDetails;
import com.example.demo.user.repository.OrderDetailsRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	StockRepository stockRepository;

	@Autowired
	ProductService productService;

	@Autowired
	SpecialOfferService soService;
	
	@Autowired
	OrderDetailsRepository orderdetailsRepo;

	@Override
	public Optional<Product> getProductById(long id) {
		return productRepository.findById(id);
	}

	@Override
	public Product create(Product product, StockDTO stockdto, MultipartFile file) {
		Product newProduct = new Product();

		newProduct.setName(product.getName());
		newProduct.setDescription(product.getDescription());
		newProduct.setPrice(product.getPrice());

		LocalDateTime date = LocalDateTime.now();
		newProduct.setDate(date);

		newProduct.setBrand(product.getBrand());
		newProduct.setCategory(product.getCategory());

		Stock stock = new Stock();
		stock.setQuantity(stockdto.getQuantity());
		stock.setDate(date);
		stockRepository.save(stock);

		newProduct.setStock(stock);

		if (file.isEmpty()) {
			newProduct.setImage(null);
		} else {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if (fileName.contains("..")) {
				System.out.println("not a valid file");
			}
			try {
				newProduct.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		newProduct.setSpecialOffer(false);

		return productRepository.save(newProduct);
	}

	@Override
	public Product update(long id, Product product, Stock stockdto, MultipartFile file) {
		Product currentProduct = productService.getProductById(id).get();

		currentProduct.setName(product.getName());
		currentProduct.setDescription(product.getDescription());
		currentProduct.setPrice(product.getPrice());
		currentProduct.setBrand(product.getBrand());
		currentProduct.setCategory(product.getCategory());

		Stock stock = currentProduct.getStock();
		stock.setQuantity(stockdto.getQuantity());
		LocalDateTime date = LocalDateTime.now();
		stock.setDate(date);
		stockRepository.save(stock);

		if (file.isEmpty()) {
			return productRepository.save(currentProduct);
		} else {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if (fileName.contains("..")) {
				System.out.println("not a valid file");
			}
			try {
				currentProduct.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return productRepository.save(currentProduct);
		}
	}

	@Override
	public void delete(long id) {
		productRepository.deleteById(id);
	}

	@Override
	public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return productRepository.findAll(pageable);
	}

	@Override
	public Page<Product> findByNamePaginated(int pageNo, int pageSize, String sortField, String sortDirection,
			String name) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		if (name != null) {
			return productRepository.findByNameLikeIgnoreCase("%" + name + "%", pageable);

		}

		return productRepository.findAll(pageable);
	}

	@Override
	public boolean checkIfProductIsSetToSpecialOffer(Long id) {
		SpecialOffer checkOfferProduct = soService.getOfferById(1L).get();
		Product checkProduct = productService.getProductById(id).get();
		Product specialOfferProduct = checkOfferProduct.getProduct();

		if (specialOfferProduct == checkProduct) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Product> showLast3Entries() {
		return productRepository.findTop3ByOrderByDateDesc();
	}

	@Override
	public List<Product> showLast8Entries() {
		return productRepository.findTop8ByOrderByDateDesc();
	}

	@Override
	public boolean isProductAvailable(Long id) {
		Product product = getProductById(id).get();
		Stock stock = product.getStock();
		
		int available = stock.getQuantity();
		if (available <= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Product showLastProduct() {
		return productRepository.findTop1ByOrderByDateDesc();
	}

	@Override
	public Product getTopSellingProduct() {
		List<Product> product = productRepository.findAll();
		
		Product topProduct = null;
		
		int i = 0;
		
		for (Product list : product) {
			List<OrderDetails> orders = orderdetailsRepo.findByProduct(list);
			int temp = 0;
			
			for (OrderDetails orderdetailsList : orders) {
				temp += orderdetailsList.getQuantity();
			}
			
			if(i < temp) {
				topProduct = list;
				i = temp;
			}
		}
		
		return topProduct;
	}

	@Override
	public Page<Product> findPaginatedSortedByPriceAsc(int pageNo, int pageSize, String sortField,
			String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return productRepository.findAllByOrderByPriceAsc(pageable);
	}

	@Override
	public Page<Product> findPaginatedSortedByPriceDesc(int pageNo, int pageSize, String sortField,
			String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return productRepository.findAllByOrderByPriceDesc(pageable);
	}

}
