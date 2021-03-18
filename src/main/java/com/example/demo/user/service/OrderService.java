package com.example.demo.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.demo.user.entity.AddressCreateRequest;
import com.example.demo.user.entity.CreditCard;
import com.example.demo.user.entity.Order;
import com.example.demo.user.entity.OrderDetails;
import com.example.demo.user.entity.User;

public interface OrderService {

	
	String createCCForPayment(CreditCard card, BindingResult result, User user, Model model);
	
	public String enterAddressForPayment(AddressCreateRequest request, BindingResult result, User user, Model model);
	
	Order placeOrder(AddressCreateRequest addressRequest, CreditCard ccrequest, boolean ccsave,
			boolean addressSave, User user, boolean payOnAddress);
	
	List<OrderDetails> listOfOrderDetailsByOrder(Order order);
	
	Optional<Order> getOrderById(Long id);
	
	void confirmOrderPayment(Long id);
	void confirmOrderShippment(Long id);
	
	
	int numberOfAllOrders();
	int numberOfAllOrdersWaitingForShippment();
	int numberOfAllOrdersWaitingForConfirmation();
	int numberOfAllSuccessfullOrders();
	int ordersTotalIncomeOfSuccessfullPayment();
	int ordersTotalTurnOver();
	
	
	Page<Order> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	Page<Order> findPaginatedWaitingShippment(int pageNo, int pageSize, String sortField, String sortDirection);
	Page<Order> findPaginatedWaitingConfirmation(int pageNo, int pageSize, String sortField, String sortDirection);
	Page<Order> findPaginatedComppletedOrders(int pageNo, int pageSize, String sortField, String sortDirection);
	
	
	List<Order> getLast5Orders();
	List<Order> getLast4Orders();
	List<Order> getLast3Orders();
	List<Order> getLast2Orders();
	List<Order> getLast1Orders();
	List<Order> getLastOrdersForAdminPage();
	
	List<Order> getOrdersForUserByDateDesc(User user);

}
