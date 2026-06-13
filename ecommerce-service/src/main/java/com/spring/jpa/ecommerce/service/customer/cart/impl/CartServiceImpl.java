package com.spring.jpa.ecommerce.service.customer.cart.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.customresponse.ResourceNotFoundException;
import com.spring.jpa.ecommerce.dto.CartItemDto;
import com.spring.jpa.ecommerce.dto.CartResponseDto;
import com.spring.jpa.ecommerce.model.CartItem;
import com.spring.jpa.ecommerce.model.Product;
import com.spring.jpa.ecommerce.model.User;
import com.spring.jpa.ecommerce.repository.CartItemRepository;
import com.spring.jpa.ecommerce.repository.ProductRepository;
import com.spring.jpa.ecommerce.repository.UserRepository;
import com.spring.jpa.ecommerce.service.customer.cart.CartService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public String addProductToCart(Long userId, Long productId) {
		log.info("Add product to cart for user ID: {}", userId, productId);
		
		Optional<CartItem> activeCartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
		if(activeCartItem.isPresent()) {
			// SCENARIO 1: Update quantity
			CartItem existingItems = activeCartItem.get();
			existingItems.setQuantity(existingItems.getQuantity() + 1);
			cartItemRepository.save(existingItems);
			
			log.info("Increased quantity for product ID: {}", productId);
			return "Product quantity updated in cart.";
		}else {
			// SCENARIO 2: Create new entry
			User user = userRepository.findById(userId)
										.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
			
			CartItem newItem = CartItem.builder()
										.user(user)
										.product(product)
										.quantity(1)
										.build();		
			cartItemRepository.save(newItem);
			log.info("New product added to cart: {}", product.getName());
			return "Product added to cart successfully.";
		}
	}

	@Override
	public CartResponseDto getCartItemsByUserId(Long userId) {
		log.info("Fetching cart items for user ID: {}", userId);
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
		
		List<CartItemDto> cartItemsDto = cartItems.stream()
													.map(item -> item.getCartDto())
													.toList();
		
		Double totAmount = cartItemsDto.stream()
										.mapToDouble(item -> item.getPrice() * item.getQuantity())
										.sum();
		return CartResponseDto.builder()
								.cartItems(cartItemsDto)
								.totalAmount(totAmount)
								.totalItems(cartItemsDto.size())
								.build();
	}

	@Override
	@Transactional
	public CartItemDto updateQuantity(Long userId, Long productId, Integer amount) {
		int rowsUpdated = cartItemRepository.updateQuantity(userId, productId, amount);
		
	    if (rowsUpdated == 0) {
	        Optional<CartItem> item = cartItemRepository.findByUserIdAndProductId(userId, productId);
	        if (item.isPresent() && (item.get().getQuantity() + amount <= 0)) {
	            cartItemRepository.deleteByUserIdAndProductId(userId, productId);
	            return null;
	        }
	        throw new ResourceNotFoundException("Cart item not found");
	    }
	    return cartItemRepository.findByUserIdAndProductId(userId, productId)
	            .map(CartItem::getCartDto)
	            .orElse(null);
	    
//		log.info("Updating quantity to {} for User: {}, Product: {}", changeAmount, userId, productId);
//		CartItem cartItem  = cartItemRepository.findByUserIdAndProductId(userId, productId)
//												.orElseThrow(() -> new ResourceNotFoundException("Item not found in your cart"));
//		int newQuantity = cartItem.getQuantity() + changeAmount;
//		if(newQuantity <= 0) {
//			cartItemRepository.delete(cartItem);
//	        return null; // Controller will return 204 No Content
//		}		
//		cartItem.setQuantity(newQuantity);
//		CartItem updatedItem = cartItemRepository.save(cartItem);
//		log.info("Updated product ID: {} quantity to {}", productId, newQuantity);
//		return CartItemDto.builder()
//	            .productId(updatedItem.getProduct().getId())
//	            .productName(updatedItem.getProduct().getName())
//	            .quantity(updatedItem.getQuantity())
//	            .build();
	}

	@Override
	@Transactional
	public void removeProductFromCart(Long userId, Long productId) {
		CartItem cartItem  = cartItemRepository.findByUserIdAndProductId(userId, productId)
												.orElseThrow(() -> new ResourceNotFoundException("Item not found in your cart"));
		cartItemRepository.delete(cartItem);
		log.info("Product successfully removed from cart.");		
	}

	@Override
	@Transactional
	public void clearCart(Long userId) {
		List<CartItem> items = cartItemRepository.findByUserId(userId);
		if(items.isEmpty()) {
			log.warn("Attempted to clear an already empty cart for User: {}", userId);
	        return;
		}
		cartItemRepository.clearCartByUserId(userId);
		log.info("Cart cleared successfully for User: {}", userId);
	}
}