package com.spring.jpa.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jpa.ecommerce.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
	boolean existsCouponByCode(String code);
	
	Optional<Coupon> findByCode (String code);
}