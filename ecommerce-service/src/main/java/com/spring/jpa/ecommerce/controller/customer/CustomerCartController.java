package com.spring.jpa.ecommerce.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.CartItemDto;
import com.spring.jpa.ecommerce.dto.CartResponseDto;
import com.spring.jpa.ecommerce.service.customer.cart.CartService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/customer/cart")
public class CustomerCartController {

	@Autowired
	CartService cartService;	

	@PostMapping("/add/{userId}/{productId}")
	public ResponseEntity<String> addProductToCart(@PathVariable Long userId, @PathVariable Long productId){
		String message = cartService.addProductToCart(userId, productId);
		return ResponseEntity.status(HttpStatus.CREATED).body(message);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<CartResponseDto> getCartItemsByUserId(@PathVariable Long userId) {
		log.info("Received request to view cart for User ID: {}", userId);
		return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
	}
	
	@PutMapping("/update/{userId}/{productId}/{quantity}")
	public ResponseEntity<CartItemDto> updateCartItemQuantity(@PathVariable Long userId, @PathVariable Long productId, @PathVariable Integer changeAmount) {
		CartItemDto updatedCartItem = cartService.updateQuantity(userId, productId, changeAmount);		
		if(updatedCartItem == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(updatedCartItem);
	}
	
	@DeleteMapping("/remove/{userId}/{productId}")
	public ResponseEntity<String> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
		cartService.removeProductFromCart(userId, productId);	
		return ResponseEntity.ok("Product removed from cart successfully.");
	}	
	
	@DeleteMapping("/clear/{userId}")
	public ResponseEntity<String> clearCart(@PathVariable Long userId) {
		cartService.clearCart(userId);	
		return ResponseEntity.ok("Cart cleared successfully.");
	}	
}