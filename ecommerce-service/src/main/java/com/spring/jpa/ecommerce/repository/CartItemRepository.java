package com.spring.jpa.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.jpa.ecommerce.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	List<CartItem> findByUserId(Long userId);
	
	Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
	
    void deleteByUserIdAndProductId(Long userId, Long productId);
	
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
	void clearCartByUserId(Long userId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE CartItem c SET c.quantity = c.quantity + :amount " +
	       "WHERE c.user.id = :userId AND c.product.id = :productId AND (c.quantity + :amount) > 0")
	int updateQuantity(@Param("userId") Long userId, @Param("productId") Long productId, @Param("amount") int amount);
}