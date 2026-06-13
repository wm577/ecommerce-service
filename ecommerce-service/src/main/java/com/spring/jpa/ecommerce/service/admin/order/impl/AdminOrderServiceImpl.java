package com.spring.jpa.ecommerce.service.admin.order.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.customresponse.ResourceNotFoundException;
import com.spring.jpa.ecommerce.dto.OrderDto;
import com.spring.jpa.ecommerce.enums.OrderStatus;
import com.spring.jpa.ecommerce.model.Order;
import com.spring.jpa.ecommerce.model.OrderItem;
import com.spring.jpa.ecommerce.model.Product;
import com.spring.jpa.ecommerce.repository.OrderRepository;
import com.spring.jpa.ecommerce.repository.ProductRepository;
import com.spring.jpa.ecommerce.service.admin.order.AdminOrderService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminOrderServiceImpl implements AdminOrderService{
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public List<OrderDto> getAllPlacedOrders() {
		List<Order> orderList = orderRepository.findAllByStatusIn(List.of(OrderStatus.PLACED, OrderStatus.DELIVERED, OrderStatus.SHIPPED));
		return orderList.stream().map(Order::getOrderDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public OrderDto changeOrderStatus(Long orderId, String status) {
		Order optOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
		
		OrderStatus currentStatus = optOrder.getStatus();
	    OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
	    
	    if (newStatus == OrderStatus.DELIVERED && currentStatus != OrderStatus.SHIPPED) {
	        throw new IllegalStateException("Cannot mark as DELIVERED until the order is SHIPPED.");
	    }
	    if (newStatus == OrderStatus.SHIPPED && currentStatus != OrderStatus.PLACED) {
	        throw new IllegalStateException("Only PLACED orders can be marked as SHIPPED.");
	    }	    
	    
	    if (newStatus == OrderStatus.SHIPPED) {
	    	optOrder.setShippedDate(LocalDateTime.now());
	    } else if (newStatus == OrderStatus.DELIVERED) {
	    	optOrder.setDeliveredDate(LocalDateTime.now());
	    }
	    
	    optOrder.setStatus(newStatus);
	    return orderRepository.save(optOrder).getOrderDto();
	}
	
	@Override
	@Transactional
	public OrderDto cancelOrder(Long orderId) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	 
	    if (order.getStatus() == OrderStatus.CANCELLED) {
	        log.warn("Order {} is already cancelled. No action taken.", orderId);
	        return order.getOrderDto();
	    }

	    // 3. Logic: Only restore stock if the order was in a state that previously deducted it
	    // (Usually PLACED or SHIPPED)
	    log.info("Cancelling order {} and restoring stock for {} items", orderId, order.getOrderItems().size());
	    
	    for (OrderItem item : order.getOrderItems()) {
	        Product product = item.getProduct();       	        
	        product.restoreStock(item.getQuantity()); 	        
	        productRepository.save(product);
	    }

	    order.setStatus(OrderStatus.CANCELLED);
	    Order updatedOrder = orderRepository.save(order);

	    return updatedOrder.getOrderDto();
	}
	
	@Override
    public OrderDto getOrderById(Long orderId) {
        log.info("Fetching order details for admin, ID: {}", orderId);
        
        return orderRepository.findById(orderId)
                .map(Order::getOrderDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + orderId + " not found"));
    }
}