package com.spring.jpa.ecommerce.service.customer.cart;

import com.spring.jpa.ecommerce.dto.CartItemDto;
import com.spring.jpa.ecommerce.dto.CartResponseDto;

public interface CartService {
	String addProductToCart(Long userId, Long productId);
	
	CartResponseDto getCartItemsByUserId(Long userId);
	
	CartItemDto updateQuantity(Long userId, Long productId, Integer changeAmount);
	
	void removeProductFromCart(Long userId, Long productId);
	
	// Clears the entire cart (useful for checkout)
    void clearCart(Long userId);
}