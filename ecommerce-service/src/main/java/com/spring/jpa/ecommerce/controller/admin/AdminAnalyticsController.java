package com.spring.jpa.ecommerce.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.ecommerce.dto.AnalyticsResponseDto;
import com.spring.jpa.ecommerce.service.admin.analytics.AnalyticsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/admin/analytics")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminAnalyticsController {

	@Autowired
	AnalyticsService analyticsService;
	
	@GetMapping("/summary")
	public ResponseEntity<AnalyticsResponseDto> getDashboardSummary(){
		return ResponseEntity.ok(analyticsService.calculateAnalytics());
	}
}