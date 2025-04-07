package com.example.InventoryManagementSystem.entity;


import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(unique = true)
    private String name;


    @NotBlank(message = "Sku is required")
    @Column(unique = true)
    private String sku;

    @Positive(message = "Product price must be a positive value")
//    @Column(unique = true)
    private BigDecimal price;

    @Min(value = 0, message = "stock quantity cannot be lesser than zero")
    private Integer stockQuantity;

    private String description;

    private String imageUrl;
    private LocalDateTime expiryDate;
    private LocalDateTime updatedAt;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", expiryDate=" + expiryDate +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
