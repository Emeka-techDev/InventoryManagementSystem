package com.example.InventoryManagementSystem.controller;

import com.example.InventoryManagementSystem.dto.LoginRequestDTO;
import com.example.InventoryManagementSystem.dto.RegisterRequestDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {

        return ResponseEntity.ok(userService.registerUser(registerRequestDTO));

    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(userService.loginUser(loginRequestDTO));
    }
}
