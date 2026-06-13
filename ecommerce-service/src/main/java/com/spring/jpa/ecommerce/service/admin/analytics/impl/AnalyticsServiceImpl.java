package com.spring.jpa.ecommerce.service.admin.analytics.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.jpa.ecommerce.dto.AnalyticsResponseDto;
import com.spring.jpa.ecommerce.repository.OrderRepository;
import com.spring.jpa.ecommerce.service.admin.analytics.AnalyticsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService{
	
	@Autowired
    OrderRepository orderRepository;
	

	@Override
	public AnalyticsResponseDto calculateAnalytics() {
	    LocalDate now = LocalDate.now();
	    int m = now.getMonthValue();
	    int y = now.getYear();

	    // 1. Fetch Current Month Totals
	    Long currentOrders = orderRepository.countOrdersByMonth(m, y);
	    Double currentEarningsRaw = orderRepository.sumEarningsByMonth(m, y);
	    
	    // Use primitives for calculations to avoid NullPointerExceptions
	    double totalEarnings = (currentEarningsRaw != null) ? currentEarningsRaw : 0.0;
	    long totalOrders = (currentOrders != null) ? currentOrders : 0L;

	    // 2. Fetch and Process Status Counts
	    List<Object[]> statusCounts = orderRepository.countStatusByMonth(m, y);
	    long placed = 0, shipped = 0, delivered = 0;

	    for (Object[] row : statusCounts) {
	        if (row[0] == null) continue;

	        String status = row[0].toString().trim().toUpperCase();
	        long count = ((Number) row[1]).longValue(); 
	        
	        log.info("Processing Status: {} | Count: {}", status, count);
	        
	        if ("PLACED".equals(status)) placed = count;
	        else if ("SHIPPED".equals(status)) shipped = count;
	        else if ("DELIVERED".equals(status)) delivered = count;
	    }

	    // 3. Calculation logic (AOV and Growth)
	    double aov = (totalOrders > 0) ? totalEarnings / totalOrders : 0.0;
	    Double formattedAov = Math.round(aov * 100.0) / 100.0;        
	 
	    // 4. Fetch Last Month Data for Comparison
	    LocalDate lm = now.minusMonths(1);
	    Long prevOrders = orderRepository.countOrdersByMonth(lm.getMonthValue(), lm.getYear());
	    Double prevEarningsRaw = orderRepository.sumEarningsByMonth(lm.getMonthValue(), lm.getYear());
	    
	    long prevOrdersVal = (prevOrders != null) ? prevOrders : 0L;
	    long prevEarningsVal = (prevEarningsRaw != null) ? prevEarningsRaw.longValue() : 0L;

	    // 5. Final Assembly (Matching DTO constructor order)
	    return new AnalyticsResponseDto(
	        totalOrders,                      // currentMonthOrders
	        (long) totalEarnings,             // currentMonthEarnings
	        prevOrdersVal,                    // previousMonthOrders
	        prevEarningsVal,                  // previousMonthEarnings
	        calculateGrowth(totalOrders, prevOrdersVal),           // orderGrowth
	        calculateGrowth((long)totalEarnings, prevEarningsVal), // earningGrowth
	        formattedAov,                     // averageOrderValue
	        placed,                           // placed
	        shipped,                          // shipped
	        delivered                         // delivered
	    );
	}

    private Double calculateGrowth(Long current, Long previous){
        if(previous == null || previous == 0) {
            return (current != null && current > 0) ? 100.0 : 0.0;
        }
        return ((double)(current - previous) / previous) * 100;
    }
}