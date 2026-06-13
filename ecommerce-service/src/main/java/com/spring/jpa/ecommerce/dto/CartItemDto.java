package com.spring.jpa.ecommerce.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long id;
    private Integer quantity;
    
    // Product Details needed for the Cart UI
    private Long productId;
    private String productName;
    private Double price;
    private byte[] byteImg;
    
    // User identification
    private Long userId;
}