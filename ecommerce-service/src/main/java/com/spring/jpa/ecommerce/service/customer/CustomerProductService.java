package com.spring.jpa.ecommerce.service.customer;

import java.util.List;

import com.spring.jpa.ecommerce.dto.ProductDetailDto;
import com.spring.jpa.ecommerce.dto.ProductDto;

public interface CustomerProductService {
	List<ProductDto> getAllProducts(Integer minQty);

	List<ProductDto> getAllProductsByName(Integer minQty, String name);
	
	ProductDetailDto getProductDetailById(Long productId);
}