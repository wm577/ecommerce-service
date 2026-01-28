package com.spring.jpa.ecommerce.service.admin.order;

import java.util.List;

import com.spring.jpa.ecommerce.dto.OrderDto;

public interface AdminOrderService {
	List<OrderDto> getAllPlacedOrders();

    OrderDto changeOrderStatus(Long orderId, String status);
    
    OrderDto cancelOrder(Long orderId);
    
    OrderDto getOrderById(Long orderId);
}