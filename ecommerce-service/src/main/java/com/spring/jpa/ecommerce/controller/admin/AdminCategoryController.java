package com.spring.jpa.ecommerce.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.CategoryDto;
import com.spring.jpa.ecommerce.model.Category;
import com.spring.jpa.ecommerce.service.admin.AdminCategoryService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminCategoryController {
	@Autowired
	AdminCategoryService categoryService;

	@GetMapping("/category")
	public ResponseEntity<List<Category>> getAllCategories() {
	    log.info("Received request to fetch all categories");
	    List<Category> categories = categoryService.getAllCategories();
	    return ResponseEntity.ok(categories);
	}
	
	@PostMapping("/category")
	public ResponseEntity<Category> addNewCategory(@RequestBody CategoryDto categoryDto) throws Exception {
		log.info("Received request to add a new category with name: {}", categoryDto.getName());		
		Category category = categoryService.addNewCategory(categoryDto);		
		log.info("Category added with name: {}", category.getName());		
		return ResponseEntity.status(HttpStatus.CREATED).body(category);
	}
	
	@PutMapping("/category/{id}")
	public ResponseEntity<Category> updateCategoryById(@PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDto) throws Exception {
		log.info("Received request to update category with ID: {}", categoryDto.getId());
		
		Category updated = categoryService.updateCategoryById(categoryId, categoryDto);
	    return ResponseEntity.ok(updated);
	}
}