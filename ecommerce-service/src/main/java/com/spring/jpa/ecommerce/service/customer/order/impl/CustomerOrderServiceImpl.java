package com.spring.jpa.ecommerce.service.customer.order.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.jpa.ecommerce.customresponse.ResourceNotFoundException;
import com.spring.jpa.ecommerce.dto.OrderDto;
import com.spring.jpa.ecommerce.enums.OrderStatus;
import com.spring.jpa.ecommerce.model.CartItem;
import com.spring.jpa.ecommerce.model.Coupon;
import com.spring.jpa.ecommerce.model.Order;
import com.spring.jpa.ecommerce.model.OrderItem;
import com.spring.jpa.ecommerce.model.Product;
import com.spring.jpa.ecommerce.model.User;
import com.spring.jpa.ecommerce.repository.CartItemRepository;
import com.spring.jpa.ecommerce.repository.CouponRepository;
import com.spring.jpa.ecommerce.repository.OrderRepository;
import com.spring.jpa.ecommerce.repository.UserRepository;
import com.spring.jpa.ecommerce.service.customer.order.CustomerOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService{
	
	private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
	
	@Override
	@Transactional
	public OrderDto placeOrder(Long userId, String couponCode) {
		
		User user = userRepository.findById(userId)
								.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
		if(cartItems.isEmpty()){
			throw new IllegalStateException("Cannot place order with an empty cart");
		}
		
		double totalAmount = cartItems.stream()
										.mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
										.sum();
		
		double discount = 0.0;
		Coupon coupon = null;
		if(couponCode != null && !couponCode.isEmpty()) {
			coupon = couponRepository.findByCode(couponCode)
									.orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
			discount = (totalAmount * coupon.getDiscount()) / 100.0;
		}
		
		Order order = Order.builder()
							.user(user)
							.orderDate(LocalDateTime.now())
							.status(OrderStatus.PLACED)
							.totalAmount(totalAmount)
							.discountAmount(discount)
							.netAmount(totalAmount - discount)
							.coupon(coupon)
							.build();
		
		List<OrderItem> orderItems = cartItems.stream()
	            							.map(cartItem -> {
	                Product product = cartItem.getProduct();
	                
	                log.info("Deducting stock for product: {}. Current: {}, Order: {}", 
	                         product.getName(), product.getQuantity(), cartItem.getQuantity());
	                
	                product.deductStock(cartItem.getQuantity());

	                return OrderItem.builder()
	                        .product(product)
	                        .quantity(cartItem.getQuantity())
	                        .priceAtPurchase(product.getPrice())
	                        .order(order)
	                        .build();
	            }).toList();
				
		order.setOrderItems(orderItems);
		Order saveOrder = orderRepository.save(order);
		
		cartItemRepository.clearCartByUserId(userId);
		
		return saveOrder.getOrderDto();
	}

	@Override
	public List<OrderDto> getMyPlacedOrders(Long userId) {
		List<OrderStatus> statusList = List.of(OrderStatus.PLACED, 
				OrderStatus.SHIPPED, OrderStatus.DELIVERED);		
		return orderRepository.findByUserIdAndStatusIn(userId, statusList)
							.stream().map(Order::getOrderDto).collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public OrderDto getOrderDetails(Long orderId) {
	    Order order = orderRepository.findById(orderId)
	        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

	    return order.getOrderDto(); 
	}
}