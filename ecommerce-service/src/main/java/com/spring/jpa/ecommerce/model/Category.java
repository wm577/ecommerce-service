package com.spring.jpa.ecommerce.model;

import com.spring.jpa.ecommerce.dto.CategoryDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="category")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Lob
	private String description;
	

	@JsonIgnore // This prevents "dto": {...} from showing up in JSON response
	public CategoryDto getDto(){
		return CategoryDto.builder()
				.id(id)
				.name(name)
				.description(description)				
				.build();
	}
}