package com.spring.jpa.ecommerce.model;

import com.spring.jpa.ecommerce.dto.CartItemDto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    // A user can have many items in their cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Different users can have the same product in their carts
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    // Helper method to convert Entity to DTO
    public CartItemDto getCartDto() {
        return CartItemDto.builder()
                .id(this.id)
                .productId(this.product.getId())
                .productName(this.product.getName())
                .price(this.product.getPrice())
                .quantity(this.quantity)
                .byteImg(this.product.getImg()) // Sending image bytes for the UI
                .userId(this.user.getId())
                .build();
    }
}