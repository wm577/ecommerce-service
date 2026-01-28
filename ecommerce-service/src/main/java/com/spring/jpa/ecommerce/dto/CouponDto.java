package com.spring.jpa.ecommerce.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CouponDto {
	private Long id;
	
	private String name;
	
	private String code;
	
	private Long discount;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expirationDate;
}