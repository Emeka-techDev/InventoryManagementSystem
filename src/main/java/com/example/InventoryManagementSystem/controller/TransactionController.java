package com.example.InventoryManagementSystem.controller;

import com.example.InventoryManagementSystem.dto.CategoryDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.TransactionDTO;
import com.example.InventoryManagementSystem.dto.TransactionRequestDTO;
import com.example.InventoryManagementSystem.enums.TransactionStatus;
import com.example.InventoryManagementSystem.service.CategoryService;
import com.example.InventoryManagementSystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Response> restockInventory(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.restockInventory(transactionRequestDTO));
    }

    @PostMapping("/sell")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.sell(transactionRequestDTO));
    }

    @PostMapping("/return")
    public ResponseEntity<Response> sell(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequestDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String searchText
    ) {
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size, searchText));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getAllTransactionByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateTransaction(@PathVariable Long id, @RequestBody @Valid TransactionStatus transactionStatus) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, transactionStatus));
    }
}
