package com.spring.jpa.ecommerce.service.customer.impl;

import com.spring.jpa.ecommerce.service.customer.CustomerProductService;

import lombok.extern.slf4j.Slf4j;

import com.spring.jpa.ecommerce.customresponse.ResourceNotFoundException;
import com.spring.jpa.ecommerce.dto.ProductDetailDto;
import com.spring.jpa.ecommerce.dto.ProductDto;
import com.spring.jpa.ecommerce.model.Product;
import com.spring.jpa.ecommerce.repository.ProductRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerProductServiceImpl implements CustomerProductService{
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<ProductDto> getAllProducts(Integer minQty) {
		log.info("Customer is fetching all products");
		return productRepository.findAllByActiveTrueAndQuantityGreaterThan(minQty)
								.stream()
				                .map(Product::getDto)
				                .toList();
	}

	@Override
	public List<ProductDto> getAllProductsByName(Integer minQty, String name) {
		log.info("Searching for products containing name: {}", name);
		if (name == null || name.trim().isEmpty()) {
	        return getAllProducts(minQty);
	    }
		List<Product> products = productRepository.findAllByActiveTrueAndQuantityGreaterThanAndNameContainingIgnoreCase(minQty, name);
		return products.stream().map(Product::getDto).toList();			
	}

	@Override
	public ProductDetailDto getProductDetailById(Long productId) {
		log.info("Customer viewing product details for ID: {}", productId);		
		Product product = productRepository.findById(productId)
									.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
		if (product.getActive() == null || !product.getActive() || 
			product.getQuantity() != null || product.getQuantity() <= 0) {
            log.warn("Access denied: Product ID {} is unavailable (Inactive or Out of Stock)", productId);
            throw new ResourceNotFoundException("This product is currently unavailable.");
        }
		return product.getProductDetailDto();
	}
}