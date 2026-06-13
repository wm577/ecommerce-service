package com.spring.jpa.ecommerce.service.admin.impl;

import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.customresponse.ResourceAlreadyExistsException;
import com.spring.jpa.ecommerce.model.Coupon;
import com.spring.jpa.ecommerce.repository.CouponRepository;
import com.spring.jpa.ecommerce.service.admin.AdminCouponService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminCouponServiceImpl implements AdminCouponService{
	@Autowired
	CouponRepository couponRepository;
	
	@Override
	public Coupon addNewCoupon(Coupon coupon) {
	    log.info("Adding a new Coupon: {}", coupon.getName());	
	    
	    if(couponRepository.existsCouponByCode(coupon.getCode())) {
	        log.warn("Attempted to create duplicate coupon code: {}", coupon.getCode());
	        throw new ResourceAlreadyExistsException("Coupon with code " + coupon.getCode() + " already exists.");			
	    }
	    
	    if(coupon.getExpirationDate().isBefore(LocalDate.now())) {
	        throw new IllegalArgumentException("Expiration date cannot be in the past");
	    }	    
	    return couponRepository.save(coupon);		
	}

	@Override
	public List<Coupon> getAllCoupon() {
		log.info("Fetching all coupons...");
		List<Coupon> coupons = couponRepository.findAll();
		log.info("Total coupons found: {}", coupons.size());
	    return coupons;
	}
}