package com.example.InventoryManagementSystem.service.impl;

import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.TransactionDTO;
import com.example.InventoryManagementSystem.dto.TransactionRequestDTO;
import com.example.InventoryManagementSystem.entity.Product;
import com.example.InventoryManagementSystem.entity.Supplier;
import com.example.InventoryManagementSystem.entity.Transaction;
import com.example.InventoryManagementSystem.entity.User;
import com.example.InventoryManagementSystem.enums.TransactionStatus;
import com.example.InventoryManagementSystem.enums.TransactionType;
import com.example.InventoryManagementSystem.exceptions.NameValueRequiredException;
import com.example.InventoryManagementSystem.exceptions.NotFoundException;
import com.example.InventoryManagementSystem.repository.ProductRepository;
import com.example.InventoryManagementSystem.repository.SupplierRepository;
import com.example.InventoryManagementSystem.repository.TransactionRepository;
import com.example.InventoryManagementSystem.service.TransactionService;
import com.example.InventoryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public Response restockInventory(TransactionRequestDTO transactionRequestDTO) {
        Long productId = transactionRequestDTO.getProductId();
        Long supplierId = transactionRequestDTO.getSupplierId();
        Integer quantity = transactionRequestDTO.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        // update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .name("purchase")
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequestDTO.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction successful")
                .build();


    }

    @Override
    public Response sell(TransactionRequestDTO transactionRequestDTO) {
        Long productId = transactionRequestDTO.getProductId();
        Integer quantity = transactionRequestDTO.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        User user = userService.getCurrentLoggedInUser();

        // update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // create a transaction

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .name("purchase")
                .user(user)
//                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequestDTO.getDescription())
                .build();

        transactionRepository.save(transaction);
//
//        Transaction transaction = Transaction.builder()
//                .transactionType(TransactionType.PURCHASE)
//                .status(TransactionStatus.COMPLETED)
//                .product(product)
//                .name("purchase")
//                .user(user)
//                .totalProducts(quantity)
//                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
//                .description(transactionRequestDTO.getDescription())
//                .build();
//
//        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction successful")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequestDTO transactionRequestDTO) {

        Long productId = transactionRequestDTO.getProductId();
        Long supplierId = transactionRequestDTO.getSupplierId();
        Integer quantity = transactionRequestDTO.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        // update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .name("transaction returned")
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequestDTO.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction successful")
                .build();

    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText, pageable);

        List<TransactionDTO> transactionDTOs = modelMapper
                .map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOs.forEach(transactionDTOItem -> {
            transactionDTOItem.setUser(null);
            transactionDTOItem.setProduct(null);
            transactionDTOItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOs)
                .build();
    }

    @Override
    public Response getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("transaction not found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        transactionDTO.getUser().setTransactions(null); // removing the user transaction list

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDTO)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAllByMonthAndYear(month, year);

        List<TransactionDTO> transactionDTOS = modelMapper
                .map(transactions, new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTOItem -> {
            transactionDTOItem.setUser(null);
            transactionDTOItem.setProduct(null);
            transactionDTOItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();

    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("transaction not found"));

        transaction.setStatus(transactionStatus);
        transaction.setUpdatedAt(LocalDateTime.now());
      transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("transaction status successfully updated")
                .build();


    }

    @Override
    public Response deleteCategory(Long id) {
        return null;
    }
}
