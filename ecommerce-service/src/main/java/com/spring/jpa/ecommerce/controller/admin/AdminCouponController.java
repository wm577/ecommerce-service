package com.spring.jpa.ecommerce.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.model.Coupon;
import com.spring.jpa.ecommerce.service.admin.AdminCouponService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/admin/coupon")
public class AdminCouponController {
	
	@Autowired
	AdminCouponService couponService;
	
	@PostMapping
	public ResponseEntity<?> addNewCoupon(@RequestBody Coupon coupon) {
		log.info("Received request to add a new coupon with code: {}", coupon.getCode());
		
		if(coupon.getCode() == null) {
			log.warn("Coupon code is empty");			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Coupon code is required"));
		}
		
		try {
			log.info("Creating coupon: {}", coupon);
			Coupon couponToAdd = couponService.addNewCoupon(coupon);
			log.info("Coupon created with ID: {}", couponToAdd.getId());
			return ResponseEntity.ok(couponToAdd);
		}catch(Exception e) {
			log.error("Validation error while creating coupon: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<List<Coupon>> getAllCoupon() {
		log.info("Received request to retrieve all coupons");
		List<Coupon> coupons =  couponService.getAllCoupon();
		log.info("Returning {} coupons", coupons.size());
		return ResponseEntity.ok(coupons);
	}
}