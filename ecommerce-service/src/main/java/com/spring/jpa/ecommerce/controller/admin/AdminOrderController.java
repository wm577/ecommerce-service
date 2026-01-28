package com.spring.jpa.ecommerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.OrderDto;
import com.spring.jpa.ecommerce.service.admin.order.AdminOrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/admin/order")
public class AdminOrderController {
	@Autowired
	AdminOrderService adminOrderService;

	@GetMapping("/placed")
	public ResponseEntity<List<OrderDto>> getAllOrders(){
		log.info("Admin request: Retrieving all placed orders");
		List<OrderDto> orderList = adminOrderService.getAllPlacedOrders();		
		log.info("Order count retrieved: {}", orderList.size());	
		return ResponseEntity.ok(orderList);
	}
	
	@PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> addStock(@PathVariable Long orderId, @RequestParam String status) {
		log.info("Admin request: Changing status of order {} to {}", orderId, status);
		OrderDto orderDto = adminOrderService.changeOrderStatus(orderId, status);
		if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }
	
	@GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto orderDto = adminOrderService.getOrderById(orderId);
        return orderDto != null ? ResponseEntity.ok(orderDto) : ResponseEntity.notFound().build();
    }
}
