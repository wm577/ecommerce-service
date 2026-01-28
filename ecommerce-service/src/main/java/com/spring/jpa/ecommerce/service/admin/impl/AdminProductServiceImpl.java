package com.spring.jpa.ecommerce.service.admin.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.customresponse.ResourceNotFoundException;
import com.spring.jpa.ecommerce.dto.ProductDto;
import com.spring.jpa.ecommerce.model.Category;
import com.spring.jpa.ecommerce.model.Product;
import com.spring.jpa.ecommerce.repository.CategoryRepository;
import com.spring.jpa.ecommerce.repository.ProductRepository;
import com.spring.jpa.ecommerce.service.admin.AdminProductService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminProductServiceImpl implements AdminProductService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public ProductDto addNewProduct(ProductDto productDto){
		log.info("Adding a new product: {}", productDto.getName());
		
		Category category = categoryRepository.findById(productDto.getCategoryId())
											 .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDto.getCategoryId()));
		try {
				Product product = Product.builder()
										.name(productDto.getName())
										.description(productDto.getDescription())
										.price(productDto.getPrice())
										.img(productDto.getImg() != null ? productDto.getImg().getBytes() : null)
										.quantity(productDto.getQuantity() != null ? productDto.getQuantity() : 0)
										.active(true)
										.category(category)
										.build();
				Product savedProduct = productRepository.save(product);
				return savedProduct.getDto();
		} catch (IOException e) {
		        log.error("Error processing product image: {}", e.getMessage());
		        throw new RuntimeException("Failed to process product image file");
	    }
	}
	
	@Override
	@Transactional
	public ProductDto updateProduct(Long productId, ProductDto productDto){
		Product product = productRepository.findById(productId)
													.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
		Category category = categoryRepository.findById(productDto.getCategoryId())
												.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDto.getCategoryId()));
		try {
	        product.setName(productDto.getName());
	        product.setPrice(productDto.getPrice());
	        product.setDescription(productDto.getDescription());
	        product.setCategory(category);
	        
	        if (productDto.getImg() != null && !productDto.getImg().isEmpty()) {
	            product.setImg(productDto.getImg().getBytes());
	        }	        
	        if (productDto.getQuantity() != null) {
	            product.setQuantity(productDto.getQuantity());
	        }
	        if (productDto.getIsActive() != null) {
	        	product.setActive(productDto.getIsActive());
	        }
	        return productRepository.save(product).getDto();
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to update product image");
	    }
	}
	
	@Override
	@Transactional
	public ProductDto addStockQuantity(Long productId, Integer incomingStockQty){
		Product product = productRepository.findById(productId)
											.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
		log.info("Old Qty {} ", product.getQuantity());	  
		int oldQty = product.getQuantity();
	    int newQty = oldQty + incomingStockQty;
	    log.info("Updating stock for ID {}: {} + {} = {}", productId, oldQty, incomingStockQty, newQty);	    
	    product.setQuantity(newQty);
	    return product.getDto();
	}

	@Override
	public List<ProductDto> getAllProducts() {
		log.info("Fetching all products from the database.");
		
		List<Product> products = productRepository.findAll();
		if (products.isEmpty()) {
			log.warn("Search result: No products found in the database.");
	        throw new ResourceNotFoundException("No products available in the database.");
	    }
		log.info("Successfully retrieved {} products.", products.size());
		return products.stream()
		                .map(Product::getDto)
		                .toList();
	}

	@Override
	public ProductDto getProductById(Long productId) {
		log.info("Get product by ID {}. " + productId);
		
		Optional<Product> products = productRepository.findById(productId);
		if (products.isEmpty()) {
	        log.warn("No products found in the database.");
	        throw new ResourceNotFoundException("No products are currently available in the store.");
	    }
		return products.map(Product::getDto)
						.orElse(null);
	}

	@Override
	public List<ProductDto> getAllProductsByName(String name) {
		log.info("Searching for products containing name: {}", name);
				
		List<Product> products = productRepository.findAllByNameContainingIgnoreCase(name);
		if (products.isEmpty()) {
			throw new ResourceNotFoundException("No products found with name: " + name);
	    }	    
	    return products.stream().map(Product::getDto).toList();				
	}

	@Override
	public void deleteProductById(Long productID) {
		Product productId = productRepository.findById(productID)
											.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productID));
		productRepository.delete(productId);
		log.info("Product with ID: {} deleted successfully", productId);
	}
}