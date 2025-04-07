package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.CategoryDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.TransactionRequestDTO;
import com.example.InventoryManagementSystem.enums.TransactionStatus;

public interface TransactionService {
    Response restockInventory(TransactionRequestDTO transactionRequestDTO);

    Response sell(TransactionRequestDTO transactionRequestDTO);

    Response returnToSupplier(TransactionRequestDTO transactionRequestDTO);

    Response getAllTransactions(int page, int size, String searchText);

    Response getTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);

    Response deleteCategory(Long id);
}
