package com.spring.jpa.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jpa.ecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	boolean existsCategoryByName(String code);
}