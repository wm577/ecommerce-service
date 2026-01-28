package com.spring.jpa.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.jpa.ecommerce.enums.OrderStatus;
import com.spring.jpa.ecommerce.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserIdAndStatusIn(Long userId, List<OrderStatus> status);
	
	List<Order> findAllByStatusIn(List<OrderStatus> orderStatusList);
	
	@Query(value = "SELECT COUNT(*) FROM orders WHERE MONTH(order_date) = :month AND YEAR(order_date) = :year", nativeQuery = true)
    Long countOrdersByMonth(@Param("month") int month, @Param("year") int year);

    @Query(value = "SELECT SUM(total_amount) FROM orders WHERE MONTH(order_date) = :month AND YEAR(order_date) = :year", nativeQuery = true)
    Double sumEarningsByMonth(@Param("month") int month, @Param("year") int year);

    @Query(value = "SELECT status, COUNT(*) FROM orders WHERE MONTH(order_date) = :month AND YEAR(order_date) = :year GROUP BY status", nativeQuery = true)
    List<Object[]> countStatusByMonth(@Param("month") int month, @Param("year") int year);
}