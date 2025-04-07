package com.example.InventoryManagementSystem.service.impl;

import com.example.InventoryManagementSystem.dto.CategoryDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.SupplierDTO;
import com.example.InventoryManagementSystem.entity.Category;
import com.example.InventoryManagementSystem.entity.Supplier;
import com.example.InventoryManagementSystem.exceptions.NotFoundException;
import com.example.InventoryManagementSystem.repository.CategoryRepository;
import com.example.InventoryManagementSystem.repository.SupplierRepository;
import com.example.InventoryManagementSystem.service.CategoryService;
import com.example.InventoryManagementSystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {
        Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);
        Supplier savedSupplier = supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(201)
                .message("supplier added successfully")
                .build();
    }

    @Override
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        List<SupplierDTO> supplierDTOS = modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .supplierDTOS(supplierDTOS)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("supplier Not Found"));

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("supplier retrieved successfully")
                .supplierDTO(supplierDTO)
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("supplier not found"));

        if (supplierDTO.getName() != null) supplier.setName(supplierDTO.getName());
        if (supplierDTO.getAddress() != null) supplier.setAddress(supplierDTO.getAddress());

        supplierRepository.save(supplier);

        SupplierDTO supplierDTO1 = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("update supplier successfully")
                .supplierDTO(supplierDTO1)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("supplier not found"));
        supplierRepository.delete(supplier);

        return Response.builder()
                .status(200)
                .message("deleted supplier successfully")
                .build();
    }
}
