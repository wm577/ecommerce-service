package com.spring.jpa.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SignupRequestDto {
	
	@NotBlank(message = "Email can't be empty.")
    @Pattern(
        regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
        message = "Please provide a valid email address (e.g., user@example.com)."
    )
	private String email;
	
	private String name;
	private String password;
}