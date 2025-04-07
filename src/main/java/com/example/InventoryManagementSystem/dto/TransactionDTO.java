package com.example.InventoryManagementSystem.dto;

import com.example.InventoryManagementSystem.entity.Product;
import com.example.InventoryManagementSystem.entity.User;
import com.example.InventoryManagementSystem.enums.TransactionStatus;
import com.example.InventoryManagementSystem.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {
    private Long id;

    private String name;

    private Integer totalProducts;

    private BigDecimal totalPrice;

    private TransactionType transactionType;

    private TransactionStatus status;

    private String description;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    private UserDTO user;

    private ProductDTO product;

    private SupplierDTO supplier;

}
