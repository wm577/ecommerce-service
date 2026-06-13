package com.spring.jpa.ecommerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.ProductDto;
import com.spring.jpa.ecommerce.service.admin.AdminProductService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminProductController {

	@Autowired
	AdminProductService productService;
	
	@PostMapping("/product")
	public ResponseEntity<ProductDto> addNewProduct(@Valid @ModelAttribute ProductDto productDto) throws Exception {
		log.info("Received request to add a new product with name: {}", productDto.getName());		
		ProductDto product = productService.addNewProduct(productDto);		
		log.info("Product added with ID: {}", product.getId());		
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}
	
	@PutMapping("/product/{productId}")
	public ResponseEntity<ProductDto> updateProductById(@PathVariable Long productId, @ModelAttribute ProductDto productDto) {
		log.info("Received request to update product by ID: {} " + productId);
		ProductDto updatedProduct = productService.updateProduct(productId, productDto);
		return ResponseEntity.ok(updatedProduct);
	}
	
	@PutMapping("/{productId}/add-stock")
    public ResponseEntity<ProductDto> addStock(@PathVariable Long productId, @RequestParam("quantity") Integer quantity) {
        
        log.info("Request to increase stock for Product ID: {} by amount: {}", productId, quantity);
        
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        ProductDto updatedProduct = productService.addStockQuantity(productId, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductDto>> getAllProduct(){
		log.info("Received request to retrieve all products");
		List<ProductDto> productList = productService.getAllProducts();		
		log.info("Product count: {}", productList.size());		
		return ResponseEntity.ok(productList);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
		log.info("Received request to retrieve product by ID {} " + productId);
		ProductDto products = productService.getProductById(productId);
		if(products != null) {
			log.info("Returning product with ID: {}", productId);
			return ResponseEntity.ok(products);
		}else {
			log.warn("Product with ID: {} not found", productId);
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/search/{name}")
	public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable String name) {
		log.info("Searching for: {}", name);
	    List<ProductDto> products = productService.getAllProductsByName(name);
	    return ResponseEntity.ok(products);
	}	
	
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<String> deleteProductById(@PathVariable Long productId) {
		productService.deleteProductById(productId);		
		return ResponseEntity.ok("Product deleted successfully with ID: " + productId);
	}
}