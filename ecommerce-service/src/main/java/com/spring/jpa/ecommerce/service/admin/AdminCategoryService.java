package com.spring.jpa.ecommerce.service.admin;

import java.util.List;

import com.spring.jpa.ecommerce.dto.CategoryDto;
import com.spring.jpa.ecommerce.model.Category;

public interface AdminCategoryService {
	Category addNewCategory(CategoryDto categoryDto);
	
	Category updateCategoryById(Long categoryId, CategoryDto categoryDto);
	
	List<Category> getAllCategories();
}