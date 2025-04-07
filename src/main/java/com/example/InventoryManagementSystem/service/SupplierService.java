package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.CategoryDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.SupplierDTO;

public interface SupplierService {
    Response addSupplier(SupplierDTO supplierDTO);

    Response getAllSuppliers();

    Response getSupplierById(Long id);

    Response updateSupplier(Long id, SupplierDTO supplierDTO);

    Response deleteSupplier(Long id);
}
