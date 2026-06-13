package com.spring.jpa.ecommerce.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.ProductDetailDto;
import com.spring.jpa.ecommerce.dto.ProductDto;
import com.spring.jpa.ecommerce.service.customer.CustomerProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/customer")
public class CustomerProductController {
	
	@Autowired
	CustomerProductService customerProductService;
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductDto>> getAllProduct(){
		return ResponseEntity.ok(customerProductService.getAllProducts(0));
	}
	
	@GetMapping("/search/{name}")
	public ResponseEntity<List<ProductDto>> getAllProductsByName(@PathVariable(name = "name", required = false) String name) {
		log.info("Received request to retrieve product by name {} " + name);
		return ResponseEntity.ok(customerProductService.getAllProductsByName(0, name));
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable Long productId) {
		log.info("Received request to retrieve product details by ID {} " + productId);
		return ResponseEntity.ok(customerProductService.getProductDetailById(productId));
	}
}