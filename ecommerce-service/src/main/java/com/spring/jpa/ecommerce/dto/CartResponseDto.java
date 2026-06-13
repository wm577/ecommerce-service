package com.spring.jpa.ecommerce.dto;

import java.util.List;
import lombok.*;

@Data
@Builder
public class CartResponseDto {
    private List<CartItemDto> cartItems;
    private Double totalAmount;
    private Integer totalItems;
}