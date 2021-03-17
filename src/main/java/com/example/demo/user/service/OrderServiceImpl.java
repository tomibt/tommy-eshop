package com.example.demo.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.demo.siteInfo.SiteInfo;
import com.example.demo.siteInfo.SiteInfoService;
import com.example.demo.user.entity.Address;
import com.example.demo.user.entity.AddressCreateRequest;
import com.example.demo.user.entity.CartProduct;
import com.example.demo.user.entity.CreditCard;
import com.example.demo.user.entity.Order;
import com.example.demo.user.entity.OrderAddress;
import com.example.demo.user.entity.OrderCardDetails;
import com.example.demo.user.entity.OrderDetails;
import com.example.demo.user.entity.OrderDetailsKey;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.AddressRepository;
import com.example.demo.user.repository.CartProductRepository;
import com.example.demo.user.repository.CreditCardRepository;
import com.example.demo.user.repository.OrderAddressRepository;
import com.example.demo.user.repository.OrderCardDetailsRepository;
import com.example.demo.user.repository.OrderDetailsRepository;
import com.example.demo.user.repository.OrderRepository;
import com.example.demo.user.repository.UserRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	SiteInfoService siService;

	@Autowired
	CreditCardRepository ccRepository;
	
	@Autowired
	CartProductRepository cartProductRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	OrderAddressRepository orderAddressRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CartProductService cartProductService;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	@Autowired
	OrderCardDetailsRepository cardDetailsRepository;


	@Override
	public String createCCForPayment(CreditCard card, BindingResult result, User user, Model model) {
		if (result.hasErrors()) {
			SiteInfo si = siService.getSiteInfoById(1L).get();

			model.addAttribute("si", si);
			return "korisnik/creditcardpayment";
		}

		if (user.getCc() == null) {
			CreditCard cc = new CreditCard();
			cc.setNumber(card.getNumber());
			cc.setName(card.getName());
			cc.setMonth(card.getMonth());
			cc.setYear(card.getYear());
			cc.setCcv(card.getCcv());
			ccRepository.save(cc);

			user.setCc(cc);
			userRepository.save(user);
			return "redirect:/paymentdetails";
		} else {
			CreditCard cc = user.getCc();
			cc.setNumber(card.getNumber());
			cc.setName(card.getName());
			cc.setMonth(card.getMonth());
			cc.setYear(card.getYear());
			cc.setCcv(card.getCcv());

			ccRepository.save(cc);
			return "redirect:/paymentdetails";

		}

	}

	@Override
	public String enterAddressForPayment(AddressCreateRequest request, BindingResult result, User user, Model model) {
		if (result.hasErrors()) {
			SiteInfo si = siService.getSiteInfoById(1L).get();

			model.addAttribute("si", si);
			return "korisnik/enteraddressforpayment";
		}

		if (user.getAddress() == null) {

			Address address = new Address();

			address.setCity(request.getCity());
			address.setStreet(request.getStreet());
			address.setZipCode(request.getZipCode());
			address.setCountry(request.getCountry());
			addressRepository.save(address);
			user.setAddress(address);

			userRepository.save(user);

			return "redirect:/paymentdetails";
		} else {
			Address address = user.getAddress();

			address.setCity(request.getCity());
			address.setStreet(request.getStreet());
			address.setZipCode(request.getZipCode());
			address.setCountry(request.getCountry());

			addressRepository.save(address);

			userRepository.save(user);
			return "redirect:/paymentdetails";
		}
	}

	@Override
	public Order placeOrder(AddressCreateRequest addressRequest, CreditCard ccrequest, boolean ccsave,
			boolean addressSave, User user, boolean payOnAddress) {

		Order newOrder = new Order();

		LocalDateTime date = LocalDateTime.now();
		newOrder.setDate(date);

		saveAddressForOrder(user, addressSave, addressRequest, newOrder);

		newOrder.setPayOnAddress(payOnAddress);
		
		List<CartProduct> cartItemsList = user.getCart();
		int productTotalCost = cartProductService.productTotalCost(cartItemsList);

		newOrder.setOrderTotalAmount(productTotalCost);

		setPaymentOnDoorOrCreateCard(payOnAddress, user, ccrequest, newOrder, ccsave);
		
		orderRepository.save(newOrder);

		List<CartProduct> cart = cartProductService.getCartByUser(user);

//		List<Product> productList = cartProductService.getAllProductsFromShoppingCart(user);


		for (CartProduct list : cart) {
			OrderDetails od = new OrderDetails();
			od.setId(new OrderDetailsKey());
			od.setOrder(newOrder);
			od.setProduct(list.getProduct());
			od.setQuantity(list.getQuantity());
			od.setProductPrice(list.getProduct().getPrice());
			orderDetailsRepository.save(od);
		}

		List<OrderDetails> orderDetailsList = listOfOrderDetailsByOrder(newOrder);

		newOrder.setDetails(orderDetailsList);

		newOrder.setPaymentSuccessfull(false);
		newOrder.setUser(user);

		newOrder.setShipped(false);
		
		

		return orderRepository.save(newOrder);

	}
	
	
	@Override
	public List<OrderDetails> listOfOrderDetailsByOrder(Order order){
		return orderDetailsRepository.findByOrder(order);
	}
	
	void saveAddressForOrder(User user, boolean addressSave, AddressCreateRequest addressRequest, Order newOrder) {
		if (user.getAddress() == null && addressSave == false) {
			OrderAddress address = new OrderAddress();
			address.setCity(addressRequest.getCity());
			address.setCountry(addressRequest.getCountry());
			address.setStreet(addressRequest.getStreet());
			address.setZipCode(addressRequest.getZipCode());
			orderAddressRepository.save(address);
			newOrder.setAddress(address);

			newOrder.setAddress(address);
		} else if (user.getAddress() == null && addressSave == true) {
			Address address = new Address();
			address.setCity(addressRequest.getCity());
			address.setCountry(addressRequest.getCountry());
			address.setStreet(addressRequest.getStreet());
			address.setZipCode(addressRequest.getZipCode());
			addressRepository.save(address);
			user.setAddress(address);

			userRepository.save(user);
			
			OrderAddress orderAddress = new OrderAddress();
			orderAddress.setCity(addressRequest.getCity());
			orderAddress.setCountry(addressRequest.getCountry());
			orderAddress.setStreet(addressRequest.getStreet());
			orderAddress.setZipCode(addressRequest.getZipCode());
			orderAddressRepository.save(orderAddress);
			
			newOrder.setAddress(orderAddress);
		} else if (user.getAddress() != null) {
			Address address = user.getAddress();
			OrderAddress orderAddress = new OrderAddress();
			orderAddress.setCity(address.getCity());
			orderAddress.setCountry(address.getCountry());
			orderAddress.setStreet(address.getStreet());
			orderAddress.setZipCode(address.getZipCode());
			orderAddressRepository.save(orderAddress);
			
			newOrder.setAddress(orderAddress);
		}
	}
	
	void setPaymentOnDoorOrCreateCard(boolean payOnAddress, User user, CreditCard ccrequest, Order newOrder, boolean ccsave) {
		if (payOnAddress == true) {
			newOrder.setPayOnAddress(true);
		} else {

			if (user.getCc() == null && ccsave == false) {
				CreditCard cc = new CreditCard();
				cc.setName(ccrequest.getName());
				cc.setNumber(ccrequest.getNumber());
				cc.setMonth(ccrequest.getMonth());
				cc.setYear(ccrequest.getYear());
				cc.setCcv(ccrequest.getCcv());
				
				OrderCardDetails cardDetails = new OrderCardDetails();
				String digits = getCardLastFourDigits(ccrequest);
				cardDetails.setCardHolderName(ccrequest.getName());
				cardDetails.setLastFourDigits(digits);
				cardDetailsRepository.save(cardDetails);
				
				newOrder.setCardDetails(cardDetails);
			} else if (user.getCc() == null && ccsave == true) {
				CreditCard cc = new CreditCard();
				cc.setName(ccrequest.getName());
				cc.setNumber(ccrequest.getNumber());
				cc.setMonth(ccrequest.getMonth());
				cc.setYear(ccrequest.getYear());
				cc.setCcv(ccrequest.getCcv());
				ccRepository.save(cc);
				
				user.setCc(cc);
				userRepository.save(user);
				
				OrderCardDetails cardDetails = new OrderCardDetails();
				String digits = getCardLastFourDigits(ccrequest);
				cardDetails.setCardHolderName(ccrequest.getName());
				cardDetails.setLastFourDigits(digits);
				cardDetailsRepository.save(cardDetails);
				
				newOrder.setCardDetails(cardDetails);
			} else if (user.getCart() != null) {
				CreditCard cc = user.getCc();

				OrderCardDetails cardDetails = new OrderCardDetails();
				String digits = cc.getNumber().substring(cc.getNumber().length() - 4);
				cardDetails.setCardHolderName(cc.getName());
				cardDetails.setLastFourDigits(digits);
				cardDetailsRepository.save(cardDetails);
				
				newOrder.setCardDetails(cardDetails);
			}
		}
	}
	
	public String getCardLastFourDigits(CreditCard ccrequest) {
		
		String number = ccrequest.getNumber();
		
		String lastFourDigits = number.substring(number.length() - 4);
		
		return lastFourDigits;
	}

	@Override
	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}
	
	@Override
	public void confirmOrderPayment(Long id) {
		Order order = getOrderById(id).get();
		order.setPaymentSuccessfull(true);
		orderRepository.save(order);
		
	}
	
	@Override
	public void confirmOrderShippment(Long id) {
		Order order = getOrderById(id).get();
		order.setShipped(true);
		orderRepository.save(order);
	}
	
	@Override
	public int numberOfAllOrders() {
		List<Order> orders = orderRepository.findAll();
		
		int i = orders.size();
		return i;
	}
	
	@Override
	public int numberOfAllOrdersWaitingForShippment() {
		List<Order> orders = orderRepository.findByShippedFalse();
		
		int i = orders.size();
		return i;
	}
	
	@Override
	public int numberOfAllOrdersWaitingForConfirmation() {
		List<Order> orders = orderRepository.findByPaymentSuccessfullFalseAndShippedTrue();
		
		int i = orders.size();
		return i;
	}
	
	@Override
	public int numberOfAllSuccessfullOrders() {
		List<Order> orders = orderRepository.findByPaymentSuccessfullTrueAndShippedTrue();
		
		int i = orders.size();
		return i;
	}
	
	@Override
	public int ordersTotalIncomeOfSuccessfullPayment() {
		List<Order> orders = orderRepository.findByPaymentSuccessfullTrueAndShippedTrue();
		
		int sum = 0;
		for (Order list : orders) {
			sum += list.getOrderTotalAmount();
		}
		
		return sum;
	}
	
	@Override
	public int ordersTotalTurnOver() {
		List<Order> orders = orderRepository.findAll();
		
		int sum = 0;
		for (Order list : orders) {
			sum += list.getOrderTotalAmount();
		}
		
		return sum;
	}

	@Override
	public Page<Order> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return orderRepository.findAll(pageable);
	}

	@Override
	public Page<Order> findPaginatedWaitingShippment(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return orderRepository.findByShippedFalse(pageable);
	}

	@Override
	public Page<Order> findPaginatedWaitingConfirmation(int pageNo, int pageSize, String sortField,
			String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return orderRepository.findByPaymentSuccessfullFalseAndShippedTrue(pageable);
	}

	@Override
	public Page<Order> findPaginatedComppletedOrders(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		return orderRepository.findByPaymentSuccessfullTrueAndShippedTrue(pageable);
	}

	@Override
	public List<Order> getLast5Orders() {
		return orderRepository.findTop5ByOrderByDateDesc();
	}

	@Override
	public List<Order> getLast4Orders() {
		return orderRepository.findTop4ByOrderByDateDesc();
	}

	@Override
	public List<Order> getLast3Orders() {
		return orderRepository.findTop3ByOrderByDateDesc();
	}

	@Override
	public List<Order> getLast2Orders() {
		return orderRepository.findTop2ByOrderByDateDesc();
	}

	@Override
	public List<Order> getLast1Orders() {
		return orderRepository.findTop1ByOrderByDateDesc();
	}

	@Override
	public List<Order> getLastOrdersForAdminPage() {
		List<Order> orders = orderRepository.findAll();
		
		List<Order> topOrders = new ArrayList<>();
		
		int i = orders.size();
		
		if(i >= 5) {
			topOrders = getLast5Orders();
			return topOrders;
		} else {
			switch (i) {
			case 1:
				topOrders = getLast1Orders();
				break;
			case 2:
				topOrders = getLast2Orders();
				break;
			case 3:
				topOrders = getLast3Orders();;
				break;
			case 4:
				topOrders = getLast4Orders();
				break;

			default:
				break;
			}
			return topOrders;
		}
	}
}
