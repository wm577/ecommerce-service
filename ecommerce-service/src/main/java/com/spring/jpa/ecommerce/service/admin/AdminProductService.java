package com.spring.jpa.ecommerce.service.admin;

import java.util.List;

import com.spring.jpa.ecommerce.dto.ProductDto;

public interface AdminProductService {
	
    ProductDto addNewProduct(ProductDto productDto);

    List<ProductDto> getAllProducts();
    
    ProductDto getProductById(Long productId);

    List<ProductDto> getAllProductsByName(String name);    

    ProductDto updateProduct(Long productId, ProductDto productDto);
    
    ProductDto addStockQuantity(Long productId, Integer incomingStockQty);

    void deleteProductById(Long productId);
}