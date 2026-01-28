package com.spring.jpa.ecommerce.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.service.customer.order.CustomerOrderService;
import com.spring.jpa.ecommerce.dto.OrderDto;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/customer/order")
public class CustomerOrderController {

	@Autowired
	CustomerOrderService customerOrderService;
	
	@PostMapping("/checkout/{userId}")
	public ResponseEntity<OrderDto> checkOut(@PathVariable Long userId, 
											@RequestParam(required = false) String couponCode) {		
		OrderDto orderDto = customerOrderService.placeOrder(userId, couponCode);
	    return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
	}
	
	@GetMapping("/myorders/{userId}")
	public ResponseEntity<List<OrderDto>> getMyPlacedOrders(@PathVariable Long userId) {		
	List<OrderDto> myOrders = customerOrderService.getMyPlacedOrders(userId);
		if (myOrders.isEmpty()) {
	        return ResponseEntity.noContent().build(); // Returns 204 if no orders found
	    }
		return ResponseEntity.ok(myOrders);
	}
	
	@GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto order = customerOrderService.getOrderDetails(orderId);        
     
        return ResponseEntity.ok(order);
    }
}