package com.spring.jpa.ecommerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.jpa.ecommerce.service.auth.AuthService;

@Configuration
public class AdminSetupConfig {
	@Bean
    CommandLineRunner initAdmin(AuthService authService) {
        return args -> {
            authService.createAdminAccount();
        };
    }
}