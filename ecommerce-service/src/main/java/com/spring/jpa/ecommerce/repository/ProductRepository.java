package com.spring.jpa.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jpa.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	// Used by Admin: Search all products regardless of status
	List<Product> findAllByNameContainingIgnoreCase(String name);
	
	// Used by Customer: Get only products marked as active
	List<Product> findAllByActiveTrueAndQuantityGreaterThan(Integer minQty);
    
	// Used by Customer: Search for specific products, but only if they are active
    List<Product> findAllByActiveTrueAndQuantityGreaterThanAndNameContainingIgnoreCase(Integer minQty, String name);
}