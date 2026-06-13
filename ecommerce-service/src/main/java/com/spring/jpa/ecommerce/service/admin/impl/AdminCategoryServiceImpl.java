package com.spring.jpa.ecommerce.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.customresponse.ResourceAlreadyExistsException;
import com.spring.jpa.ecommerce.customresponse.ResourceNotFoundException;
import com.spring.jpa.ecommerce.dto.CategoryDto;
import com.spring.jpa.ecommerce.model.Category;
import com.spring.jpa.ecommerce.repository.CategoryRepository;
import com.spring.jpa.ecommerce.service.admin.AdminCategoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public Category addNewCategory(CategoryDto categoryDto) {
		log.info("Adding a new category: {}", categoryDto.getName());
		
		if (categoryRepository.existsCategoryByName(categoryDto.getName())) {
	        throw new ResourceAlreadyExistsException("Category with name '" + categoryDto.getName() + "' already exists.");
	    }		
		Category category = Category.builder()
									.name(categoryDto.getName())
									.description(categoryDto.getDescription())
									.build();
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategoryById(Long categoryId, CategoryDto categoryDto){
		Category existingCategory = categoryRepository.findById(categoryId)
													.orElseThrow(() -> new ResourceNotFoundException ("Category with ID: " + categoryId + " not found."));
		
		if (categoryRepository.existsCategoryByName(categoryDto.getName()) && 
		        !existingCategory.getName().equalsIgnoreCase(categoryDto.getName())) 
		{		        
		        throw new ResourceAlreadyExistsException("Category name '" + categoryDto.getName() + "' is already taken.");
		}				
		Category categoryToUpdate = existingCategory.toBuilder()
													.name(categoryDto.getName())
										            .description(categoryDto.getDescription())
										            .build();				
		log.info("Category updated successfully: {}", categoryToUpdate.getName());
		return categoryRepository.save(categoryToUpdate);
	}
	
	public List<Category> getAllCategories() {
	    return categoryRepository.findAll();
	}
}