package com.spring.jpa.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.jpa.ecommerce.dto.OrderDto;
import com.spring.jpa.ecommerce.dto.OrderItemDto;
import com.spring.jpa.ecommerce.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name="orders")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime orderDate;
   
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING) // Saves "Placed" instead of 1 in the DB
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;    
	
    private Double discountAmount;
    private Double netAmount; // Total after discount
    
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    
    public OrderDto getOrderDto() {
        List<OrderItemDto> itemDtos = this.orderItems.stream()
                .map(item -> OrderItemDto.builder()
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .quantity(item.getQuantity())
                    .priceAtPurchase(item.getPriceAtPurchase())
                    .build())
                .toList();

        return OrderDto.builder()
                .id(this.id)
                .orderDate(this.orderDate)
                .totalAmount(this.totalAmount)
                .discountAmount(this.discountAmount)
                .netAmount(this.netAmount)
                .status(this.status)
                .userName(this.user.getName())
                .shippedDate(this.shippedDate)
                .deliveredDate(this.deliveredDate)
                .couponCode(this.coupon != null ? this.coupon.getCode() : "NONE")
                .orderItems(itemDtos)
                .build();
    }
}