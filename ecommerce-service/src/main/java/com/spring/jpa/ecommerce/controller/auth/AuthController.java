package com.spring.jpa.ecommerce.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.LoginRequestDto;
import com.spring.jpa.ecommerce.dto.SignupRequestDto;
import com.spring.jpa.ecommerce.dto.UserDto;
import com.spring.jpa.ecommerce.service.auth.AuthService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	@Autowired
	AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
		log.info("Received signup request for email: {}", signupRequestDto.getEmail());
		
		UserDto createdUser = authService.createUser(signupRequestDto);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);	
	}	
	
	@PostMapping("/setup-admin")
    public ResponseEntity<String> setupAdmin() {
        authService.createAdminAccount();
        return ResponseEntity.ok("Admin setup check completed.");
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
	    log.info("Received login request for email: {}", loginRequestDto.getEmail());	    
	    UserDto userDto = authService.loginUser(loginRequestDto);
	    if (userDto == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    }
	    return ResponseEntity.ok(userDto);
	}
}