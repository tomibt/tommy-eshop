package com.example.demo.user.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.product.service.StockService;
import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoService;
import com.example.demo.user.entity.AddressCreateRequest;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.CreditCard;
import com.example.demo.user.entity.Order;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.CartProductService;
import com.example.demo.user.service.OrderService;
import com.example.demo.user.service.UserService;

@Controller
public class OrderController {

	@Autowired
	UserService userService;

	@Autowired
	SiteInfoService siService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	OrderService orderService;

	@Autowired
	CartProductService cartService;
	
	@Autowired
	StockService stockService;

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping("/paymentdetails")
	public String goToCheckOutPage(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userService.getUserByUserNameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		List<CartProduct> cartItemsList = user.getCart();

		int productTotalCost = cartService.productTotalCost(cartItemsList);
		boolean checkCCForSaving = false;

		boolean checkAddressForSaving = false;

		model.addAttribute("si", si);
		model.addAttribute("addressRequest", new AddressCreateRequest());
		model.addAttribute("order", new Order());
		model.addAttribute("ccrequest", new CreditCard());
		model.addAttribute("productTotalCost", productTotalCost);
		model.addAttribute("user", user);
		model.addAttribute("ccsave", checkCCForSaving);
		model.addAttribute("addressSave", checkAddressForSaving);

		return "korisnik/paymentdetails";
	}

//	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//	@PostMapping("/orderdetails")
//	public String orderDetails(Principal principal) {
//		String userName = principal.getName();
//		
//		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
//		
//		orderService.orderDetails(user);
//		
//		return "redirect:/paymentdetails";
//	}
//	

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/creditcardforpayment")
	public String showCCPage(Model model, Principal principal) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		model.addAttribute("request", new CreditCard());

		return "korisnik/creditcardpayment";

	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@PostMapping("/createCCPayment")
	public String createCCForPayment(@Valid @ModelAttribute("request") CreditCard request, BindingResult result,
			Principal principal, Model model) {
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		return orderService.createCCForPayment(request, result, user, model);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/editAddressForPayment")
	public String goToAddressPageForPayment(Model model, Principal principal) {
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		SiteInfo si = siService.getSiteInfoById(1L).get();

		model.addAttribute("si", si);
		model.addAttribute("user", user);

		model.addAttribute("request", new AddressCreateRequest());

		return "korisnik/enteraddressforpayment";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/editAddressForPayment")
	public String addressPagePostForPayment(@Valid @ModelAttribute("request") AddressCreateRequest request,
			BindingResult result, Principal principal, Model model) {

		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		return orderService.enterAddressForPayment(request, result, user, model);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/placeorder")
	public String placeOrder(@Valid @ModelAttribute("addressRequest") AddressCreateRequest addressRequest,
			BindingResult result, @ModelAttribute("ccrequest") CreditCard ccrequest, BindingResult result2,
			@ModelAttribute("order") Order order, BindingResult result3,
			@RequestParam(name = "ccsave", defaultValue = "false") boolean ccsave,
			@RequestParam(name = "addressSave", defaultValue = "false") boolean addressSave,
			@RequestParam(name = "payOnAddress", defaultValue = "false") boolean payOnAddress, Principal principal,
			Model model) {
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();

		if(user.getAddress() == null) {
			if(result.hasErrors()) {
				createOrderHasErrorsCode(model, user);
				return "korisnik/paymentdetails";
			}
		}
		
 		if(user.getCc() == null && payOnAddress == false) {
			if (result2.hasErrors()){
				createOrderHasErrorsCode(model, user);
				return "korisnik/paymentdetails";
			}
		}
		
		if(result3.hasErrors()) {
			createOrderHasErrorsCode(model, user);
			return "korisnik/paymentdetails";
		}
		

		orderService.placeOrder(addressRequest, ccrequest, ccsave, addressSave, user, payOnAddress);
		stockService.substractStockQuantityAfterOrder(user);
		cartService.deleteAll(user);

		return "redirect:/placedorder";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/placedorder")
	public String goToPlacedOrderPage(Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();
		
		model.addAttribute("si", si);
		
		return "korisnik/placedorder";
	}
	
	void createOrderHasErrorsCode(Model model, User user) {
		SiteInfo si = siService.getSiteInfoById(1L).get();
		boolean checkCCForSaving = false;

		boolean checkAddressForSaving = false;
		List<CartProduct> cartItemsList = user.getCart();

		int productTotalCost = cartService.productTotalCost(cartItemsList);
		model.addAttribute("si", si);
		model.addAttribute("addressRequest", new AddressCreateRequest());
		model.addAttribute("order", new Order());
		model.addAttribute("ccrequest", new CreditCard());
		model.addAttribute("user", user);
		model.addAttribute("ccsave", checkCCForSaving);
		model.addAttribute("addressSave", checkAddressForSaving);
		model.addAttribute("productTotalCost", productTotalCost);
		
	}
	
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/orders")
	public String goToOrdersPage(Model model, Principal principal) {
		SiteInfo si = siService.getSiteInfoById(1L).get();
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		
		List<Order> listOrders = user.getOrders();
		
		
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("si", si);
		
		return "korisnik/orders";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/orderdetails/{id}")
	public String goToOrderDetailsPage(@PathVariable("id") Long id, Principal principal, Model model) {
		SiteInfo si = siService.getSiteInfoById(1L).get();
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		
		Order order = orderService.getOrderById(id).get();
		
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("si", si);
		
		return "korisnik/orderdetails";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/order/confirm/{id}")
	public String confirmOrderRecieved(@PathVariable("id") Long id) {
		
		orderService.confirmOrderPayment(id);
		
		return "redirect:/orders";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/order/confirmshipping/{id}")
	public String confirmOrderShipped(@PathVariable("id") Long id) {
		orderService.confirmOrderShippment(id);
		
		return "redirect:/ordersmanagment";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment")
	public String goToOrdersManagmentPage(Model model) {
		
		
		return findPaginatedOrders(1, "date", "desc", model);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/page/{pageNo}")
	public String findPaginatedOrders(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Order> page = orderService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Order> orders = page.getContent();
		
		int allorders = orderService.numberOfAllOrders();
		int waitingconfirm = orderService.numberOfAllOrdersWaitingForConfirmation();
		int waitingshippment = orderService.numberOfAllOrdersWaitingForShippment();
		int allsuccorders = orderService.numberOfAllSuccessfullOrders();
		int totalincomesucc = orderService.ordersTotalIncomeOfSuccessfullPayment();
		int totalturnover = orderService.ordersTotalTurnOver();
		
		
		model.addAttribute("allorders", allorders);
		model.addAttribute("waitingconfirm", waitingconfirm);
		model.addAttribute("waitingshippment", waitingshippment);
		model.addAttribute("allsuccorders", allsuccorders);
		model.addAttribute("totalincomesucc", totalincomesucc);
		model.addAttribute("totalturnover", totalturnover);
		
		model.addAttribute("activePage", "orderManagment");

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("orders", orders);

		return "gazda/ordersmanagment";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/details/{id}")
	public String goToOrderManagmentDetailsPage(@PathVariable("id") Long id, Principal principal, Model model) {
		String userName = principal.getName();

		User user = userRepo.findByUsernameOrEmailIgnoreCase(userName, userName).get();
		
		Order order = orderService.getOrderById(id).get();
		
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		
		return "gazda/orderdetailsmanagment";
	}
	
	//------------------------Paging orders waiting shippment---------------
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/waitingshippment")
	public String goToOrdersManagmentPageWaitingForShippment(Model model) {
		
		
		return findPaginatedOrdersWaitingForShippment(1, "date", "desc", model);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/waitingshippment/page/{pageNo}")
	public String findPaginatedOrdersWaitingForShippment(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Order> page = orderService.findPaginatedWaitingShippment(pageNo, pageSize, sortField, sortDir);
		List<Order> orders = page.getContent();
		
		int allorders = orderService.numberOfAllOrders();
		int waitingconfirm = orderService.numberOfAllOrdersWaitingForConfirmation();
		int waitingshippment = orderService.numberOfAllOrdersWaitingForShippment();
		int allsuccorders = orderService.numberOfAllSuccessfullOrders();
		int totalincomesucc = orderService.ordersTotalIncomeOfSuccessfullPayment();
		int totalturnover = orderService.ordersTotalTurnOver();
		
		
		model.addAttribute("allorders", allorders);
		model.addAttribute("waitingconfirm", waitingconfirm);
		model.addAttribute("waitingshippment", waitingshippment);
		model.addAttribute("allsuccorders", allsuccorders);
		model.addAttribute("totalincomesucc", totalincomesucc);
		model.addAttribute("totalturnover", totalturnover);
		
		model.addAttribute("activePage", "orderManagment");

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("orders", orders);

		return "gazda/ordersmanagmentwaitingshippment";
	}
	
	
	//------------------------Orders waiting user confirmation paging---------------------------
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/waitingconfirmation")
	public String goToOrdersManagmentPageWaitingForConfirmation(Model model) {
		
		
		return findPaginatedOrdersWaitingForConfirmation(1, "date", "desc", model);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/waitingconfirmation/page/{pageNo}")
	public String findPaginatedOrdersWaitingForConfirmation(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Order> page = orderService.findPaginatedWaitingConfirmation(pageNo, pageSize, sortField, sortDir);
		List<Order> orders = page.getContent();
		
		int allorders = orderService.numberOfAllOrders();
		int waitingconfirm = orderService.numberOfAllOrdersWaitingForConfirmation();
		int waitingshippment = orderService.numberOfAllOrdersWaitingForShippment();
		int allsuccorders = orderService.numberOfAllSuccessfullOrders();
		int totalincomesucc = orderService.ordersTotalIncomeOfSuccessfullPayment();
		int totalturnover = orderService.ordersTotalTurnOver();
		
		
		model.addAttribute("allorders", allorders);
		model.addAttribute("waitingconfirm", waitingconfirm);
		model.addAttribute("waitingshippment", waitingshippment);
		model.addAttribute("allsuccorders", allsuccorders);
		model.addAttribute("totalincomesucc", totalincomesucc);
		model.addAttribute("totalturnover", totalturnover);
		
		model.addAttribute("activePage", "orderManagment");

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("orders", orders);

		return "gazda/ordersmanagmentwaitingconfirmation";
	}
	
	
	//----------------------------Orders Completed-------------------
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/completed")
	public String goToOrdersManagmentPageComplited(Model model) {
		
		
		return findPaginatedOrdersCompleted(1, "date", "desc", model);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/ordersmanagment/completed/page/{pageNo}")
	public String findPaginatedOrdersCompleted(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 10;

		Page<Order> page = orderService.findPaginatedComppletedOrders(pageNo, pageSize, sortField, sortDir);
		List<Order> orders = page.getContent();
		
		int allorders = orderService.numberOfAllOrders();
		int waitingconfirm = orderService.numberOfAllOrdersWaitingForConfirmation();
		int waitingshippment = orderService.numberOfAllOrdersWaitingForShippment();
		int allsuccorders = orderService.numberOfAllSuccessfullOrders();
		int totalincomesucc = orderService.ordersTotalIncomeOfSuccessfullPayment();
		int totalturnover = orderService.ordersTotalTurnOver();
		
		
		model.addAttribute("allorders", allorders);
		model.addAttribute("waitingconfirm", waitingconfirm);
		model.addAttribute("waitingshippment", waitingshippment);
		model.addAttribute("allsuccorders", allsuccorders);
		model.addAttribute("totalincomesucc", totalincomesucc);
		model.addAttribute("totalturnover", totalturnover);
		
		model.addAttribute("activePage", "orderManagment");

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("orders", orders);

		return "gazda/ordersmanagmentcompleted";
	}
	
}
