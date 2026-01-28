package com.spring.jpa.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jpa.ecommerce.enums.UserRole;
import com.spring.jpa.ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findFirstByEmail(String email);
	
	Optional<User> findByRole(UserRole userRole);
}