package com.spring.jpa.ecommerce.service.auth;

import com.spring.jpa.ecommerce.dto.LoginRequestDto;
import com.spring.jpa.ecommerce.dto.SignupRequestDto;
import com.spring.jpa.ecommerce.dto.UserDto;

public interface AuthService {
	UserDto createUser(SignupRequestDto signupRequestDto);
	
	UserDto loginUser(LoginRequestDto loginRequestDto);

	Boolean hasUserWithEmail(String email);

	void createAdminAccount();
}