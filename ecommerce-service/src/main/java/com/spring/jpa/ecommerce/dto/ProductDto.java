package com.spring.jpa.ecommerce.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductDto {
	
	private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;
        
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity; 
    
    // Simply set the default here for the DTO
    @JsonProperty("isActive")
    private Boolean isActive = true;
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile img; 
    
    private byte[] byteImg; 
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    private String categoryName;
}