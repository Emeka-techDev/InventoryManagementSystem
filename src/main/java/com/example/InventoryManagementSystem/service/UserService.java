package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.LoginRequestDTO;
import com.example.InventoryManagementSystem.dto.RegisterRequestDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.UserDTO;
import com.example.InventoryManagementSystem.entity.User;

public interface UserService {
    Response registerUser(RegisterRequestDTO registerRequestDTO);

    Response loginUser(LoginRequestDTO loginRequestDTO);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
