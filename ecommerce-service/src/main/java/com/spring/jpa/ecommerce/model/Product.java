package com.spring.jpa.ecommerce.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.jpa.ecommerce.dto.ProductDetailDto;
import com.spring.jpa.ecommerce.dto.ProductDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "product")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private Double price;
	
	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Lob
    @Column(columnDefinition = "mediumblob")        // current limit : 16MB____use longblob for 4gb size (postgres)
    private byte[] img;
	
	@Builder.Default
	private Integer quantity = 0; //Current stock in warehouse
	
//	@JsonIgnore
//	private MultipartFile imageFile; 

	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private Boolean active = true; // Use Boolean instead of boolean
	
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;
    
    public void deductStock(Integer orderQuantity) {
        if (this.quantity < orderQuantity) {
            throw new RuntimeException("Insufficient stock for product: " + this.name);
        }
        this.quantity -= orderQuantity;
    }
    
    public void restoreStock(Integer orderQuantity) {
        this.quantity += orderQuantity;
    }
	
    @JsonIgnore // This prevents "dto": {...} from showing up in JSON response
	 public ProductDto getDto() {
	        return ProductDto.builder()
	                .id(this.id)  // Ensure the ID is being correctly assigned here
	                .name(this.name)
	                .price(this.price)
	                .description(this.description)
	                .byteImg(this.img)
	                .isActive(this.getActive())
	                .quantity(this.quantity)
	                .categoryId(this.category.getId())
	                .categoryName(this.category.getName())
	                .build();
	    }
    
    public ProductDetailDto getProductDetailDto() {
        ProductDto basicDto = this.getDto();
        return ProductDetailDto.builder()
                .productDto(basicDto)
                .build();
    }
}