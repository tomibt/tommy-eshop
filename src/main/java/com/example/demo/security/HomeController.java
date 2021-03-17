package com.example.demo.security;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.SpecialOffer;
import com.example.demo.product.entity.Stock;
import com.example.demo.product.service.ProductService;
import com.example.demo.product.service.SpecialOfferService;
import com.example.demo.product.service.StockService;
import com.example.demo.siteInfo.ContactMailRequest;
import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoService;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.Order;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.OrderService;
import com.example.demo.user.service.UserService;
import com.example.demo.user.service.WishListService;

/*
 * 
 * 
 * Here are all public mappings
 * 
 * 
 */

@Controller
public class HomeController {

	@Autowired
	SiteInfoService siService;

	@Autowired
	ProductService productService;

	@Autowired
	SpecialOfferService soService;

	@Autowired
	WishListService wishListService;

	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	StockService stockService;

	@GetMapping("/")
	public String homePage(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();
		String title = si.getTitleOfPage();
		
		Product lastProduct = productService.showLastProduct();
		model.addAttribute("noProducts", lastProduct);
		
		
		List<Product> last3Products = productService.showLast3Entries();
		List<Product> last8Products = productService.showLast8Entries();

		List<SpecialOffer> specialOffer = soService.getAllOffers();

		if (specialOffer.isEmpty()) {
		} else {
			SpecialOffer offer = soService.getOfferById(1L).get();
			Product product = offer.getProduct();

			model.addAttribute("offer", offer);
			model.addAttribute("productOffer", product);
		}
		

		model.addAttribute("last3Products", last3Products);
		model.addAttribute("last8Products", last8Products);
		model.addAttribute("si", si);
		model.addAttribute("title", title);
		return "index";

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/adminpanel")
	public String adminPage(Model model) {
		
		
		List<User> top5users = userService.getLastUsersForAdminPage();
		
		if(top5users.isEmpty()) {
			model.addAttribute("nousers", "No registered users.");
		} else {
			
			model.addAttribute("top5users", top5users);
		}
		
		List<Order> top5orders = orderService.getLastOrdersForAdminPage();
		
		if(top5orders.isEmpty()) {
			model.addAttribute("noorders", "No orders made yet.");
		} else {
			
			model.addAttribute("top5orders", top5orders);
		}
		
		List<Stock> top5stock = stockService.getTopStockForAdminPage();
		if(top5stock.isEmpty()) {
			model.addAttribute("nostock", "No products added to your store.");
		} else {
			
			model.addAttribute("top5stock", top5stock);
		}
		
		Product product = productService.getTopSellingProduct();
		if(product == null) {
			model.addAttribute("noproduct", "No product with top selling");
		} else {
			model.addAttribute("topproduct", product);
		}

		model.addAttribute("activePage", "adminpanel");

		return "gazda/adminpanel";
	}

	@GetMapping("/contact")
	public String goToContactPage(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);

		model.addAttribute("request", new ContactMailRequest());

		return "contact";
	}

	@GetMapping("/about")
	public String goToAboutPage(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);

		return "about";
	}

	// -------------------------Product mappings public**************************

	@GetMapping("/allproducts")
	public String goToProductsPage(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);

		return findPaginatedProductsPublic(1, "date", "asc", model);
	}

	@GetMapping("/allproducts/page/{pageNo}")
	public String findPaginatedProductsPublic(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 12;

		Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Product> products = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("products", products);

		return "products";
	}

	@GetMapping("/allproducts/product")
	public String goToProductDetailsPage(Model model, @RequestParam("id") Long id, Principal principal) {

		if (principal == null) {

			SiteInfo si = siService.getSiteInfoById(1L).get();

			Product product = productService.getProductById(id).get();
			boolean isAvailable = productService.isProductAvailable(id);

			model.addAttribute("product", product);
			model.addAttribute("isAvailable", isAvailable);
			model.addAttribute("si", si);
			model.addAttribute("request", new CartProduct());

			return "productdetails";
		} else {
			SiteInfo si = siService.getSiteInfoById(1L).get();
			
			Product product = productService.getProductById(id).get();
			boolean isAvailable = productService.isProductAvailable(id);
			
			
			String userName = principal.getName();
			User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
			boolean wishListCheck = wishListService.checkIfUserHasProductInWishList(id, user);
			model.addAttribute("wishListCheck", wishListCheck);
			model.addAttribute("product", product);
			model.addAttribute("isAvailable", isAvailable);
			model.addAttribute("si", si);
			model.addAttribute("request", new CartProduct());

			return "productdetails";
		}
	}
	
	@GetMapping("/allproducts/product/{id}")
	public String goToProductDetailsPageErrorShowing(Model model, @PathVariable("id") Long id, Principal principal) {

		if (principal == null) {

			SiteInfo si = siService.getSiteInfoById(1L).get();

			Product product = productService.getProductById(id).get();
			boolean isAvailable = productService.isProductAvailable(id);

			model.addAttribute("product", product);
			model.addAttribute("isAvailable", isAvailable);
			model.addAttribute("si", si);
			model.addAttribute("request", new CartProduct());

			return "productdetails";
		} else {
			SiteInfo si = siService.getSiteInfoById(1L).get();
			
			Product product = productService.getProductById(id).get();
			boolean isAvailable = productService.isProductAvailable(id);
			
			
			String userName = principal.getName();
			User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
			boolean wishListCheck = wishListService.checkIfUserHasProductInWishList(id, user);
			model.addAttribute("wishListCheck", wishListCheck);
			model.addAttribute("product", product);
			model.addAttribute("isAvailable", isAvailable);
			model.addAttribute("si", si);
			model.addAttribute("request", new CartProduct());

			return "productdetails";
		}
	}
	
	
	@GetMapping("/allProductsSearch")
	public String allProductsSearchResult(Model model, @RequestParam(value = "search", defaultValue = "") String search) {
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		
		return findPaginatedProductsPublicSearch(search, 1, "name", "asc", model);
		
	}
	
	@GetMapping("/allProductsSearch/page/{pageNo}")
	public String findPaginatedProductsPublicSearch(String search, @PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 12;

		Page<Product> page = productService.findByNamePaginated(pageNo, pageSize, sortField, sortDir, search);
		List<Product> products = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("products", products);
		model.addAttribute("search", search);

		return "productsearch";
	}
	
	@GetMapping("/allproducts/priceasc")
	public String allProductsOrderedByPriceAsc(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		
		return findPaginatedProductsOrderedByPriceAsc(1, "price", "asc", model);
		
	}
	
	@GetMapping("/allproducts/priceasc/page/{pageNo}")
	public String findPaginatedProductsOrderedByPriceAsc(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 12;

		Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Product> products = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("products", products);

		return "products";
	}
	
	@GetMapping("/allproducts/pricedesc")
	public String allProductsOrderedByPriceDesc(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		
		return findPaginatedProductsOrderedByPriceDesc(1, "price", "dsc", model);
		
	}
	
	@GetMapping("/allproducts/pricedesc/page/{pageNo}")
	public String findPaginatedProductsOrderedByPriceDesc(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 12;

		Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Product> products = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("products", products);

		return "products";
	}

}
