package com.example.InventoryManagementSystem.dto;


import com.example.InventoryManagementSystem.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;

    private String token;
    private UserRole role;
    private String expirationTime;

    private Integer totalPages;
    private Long totalElements;

    private UserDTO user;
    private List<UserDTO> users;

    private CategoryDTO category;
    private List<CategoryDTO> categories;

    private SupplierDTO supplierDTO;
    private List<SupplierDTO> supplierDTOS;

    private ProductDTO product;
    private List<ProductDTO> products;

    private TransactionDTO transaction;
    private List<TransactionDTO> transactions;

    private final LocalDateTime timestamp = LocalDateTime.now();
}
