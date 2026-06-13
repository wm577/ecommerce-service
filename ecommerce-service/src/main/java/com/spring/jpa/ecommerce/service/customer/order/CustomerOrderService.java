package com.spring.jpa.ecommerce.service.customer.order;

import java.util.List;

import com.spring.jpa.ecommerce.dto.OrderDto;

public interface CustomerOrderService {
	OrderDto placeOrder(Long userId, String couponCode);
	
	List<OrderDto> getMyPlacedOrders(Long userId);
	
	OrderDto getOrderDetails(Long orderId);
}