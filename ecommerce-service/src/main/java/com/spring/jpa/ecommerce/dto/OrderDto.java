package com.spring.jpa.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.spring.jpa.ecommerce.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private Double discountAmount;
    private Double netAmount;  
    
    private OrderStatus status;
    
    private String userName; // Just the name instead of the whole User object
    
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    
    private String couponCode; // Just the code
    private List<OrderItemDto> orderItems;
}