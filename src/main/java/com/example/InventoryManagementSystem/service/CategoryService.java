package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.CategoryDTO;
import com.example.InventoryManagementSystem.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}
