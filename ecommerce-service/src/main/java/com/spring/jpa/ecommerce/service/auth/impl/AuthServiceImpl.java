package com.spring.jpa.ecommerce.service.auth.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.dto.LoginRequestDto;
import com.spring.jpa.ecommerce.dto.SignupRequestDto;
import com.spring.jpa.ecommerce.dto.UserDto;
import com.spring.jpa.ecommerce.enums.UserRole;
import com.spring.jpa.ecommerce.model.User;
import com.spring.jpa.ecommerce.repository.UserRepository;
import com.spring.jpa.ecommerce.service.auth.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	@Autowired
    UserRepository userRepository;
	
	private final BCryptPasswordEncoder bEncoder;
	
	@Override
	public UserDto createUser(SignupRequestDto signupRequestDto) {
		log.info("Attempting to create user with email: {}", signupRequestDto.getEmail());
		
		if (hasUserWithEmail(signupRequestDto.getEmail())) {
	        log.warn("Signup attempt failed: Email {} is already registered", signupRequestDto.getEmail());
	        throw new RuntimeException("Email already in use"); 
	    }
		
		String encodedPassword = bEncoder.encode(signupRequestDto.getPassword());		
		User user = User.builder()
						.email(signupRequestDto.getEmail())
						.name(signupRequestDto.getName())
						.password(encodedPassword)
						.role(UserRole.CUSTOMER)
						.build();		
		User savedUser = userRepository.save(user);
		log.info("User saved successfully with ID: {}", savedUser.getId());
		
		return UserDto.builder()
	            .id(savedUser.getId())
	            .email(savedUser.getEmail())
	            .name(savedUser.getName())
	            .userRole(savedUser.getRole())
	            .build();
	}

	@Override
	public Boolean hasUserWithEmail(String email) {
		return userRepository.findFirstByEmail(email).isPresent();
	}

	@Override
	public void createAdminAccount() {
	  log.info("Running application for the first time creates an Admin account with default info");
        Optional<User> adminAccountUser = userRepository.findByRole(UserRole.ADMIN);
        if (adminAccountUser.isEmpty()) {
            log.info("Admin account created with email: admin@gmail.com and password: admin");
            userRepository.save(
                    User.builder()
                            .email("admin@gmail.com")
                            .name("admin")
                            .role(UserRole.ADMIN)
                            .password(bEncoder.encode("admin"))
                            .build()
            );
        }
	}

	@Override
	public UserDto loginUser(LoginRequestDto loginRequestDto) {
	    log.info("Attempting login for email: {}", loginRequestDto.getEmail());
	  
	    User user = userRepository.findFirstByEmail(loginRequestDto.getEmail())
	            .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequestDto.getEmail()));
	    
	    if (!bEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
	        log.warn("Login failed: Password mismatch for email {}", loginRequestDto.getEmail());
	        throw new RuntimeException("Invalid password");
	    }

	    log.info("Login successful for user: {}", user.getEmail());
	  
	    return UserDto.builder()
	            .id(user.getId())
	            .email(user.getEmail())
	            .name(user.getName())
	            .userRole(user.getRole())
	            .build();
	}
}