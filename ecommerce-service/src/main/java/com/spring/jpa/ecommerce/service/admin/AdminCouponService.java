package com.spring.jpa.ecommerce.service.admin;

import java.util.List;

import com.spring.jpa.ecommerce.model.Coupon;

public interface AdminCouponService {
	Coupon addNewCoupon(Coupon coupon);
	
	List<Coupon> getAllCoupon();
}