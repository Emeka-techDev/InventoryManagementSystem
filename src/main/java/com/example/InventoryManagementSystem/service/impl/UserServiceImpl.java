package com.example.InventoryManagementSystem.service.impl;

import com.example.InventoryManagementSystem.dto.LoginRequestDTO;
import com.example.InventoryManagementSystem.dto.RegisterRequestDTO;
import com.example.InventoryManagementSystem.dto.Response;
import com.example.InventoryManagementSystem.dto.UserDTO;
import com.example.InventoryManagementSystem.entity.User;
import com.example.InventoryManagementSystem.enums.UserRole;
import com.example.InventoryManagementSystem.exceptions.InvalidCredentialsException;
import com.example.InventoryManagementSystem.exceptions.NotFoundException;
import com.example.InventoryManagementSystem.repository.UserRepository;
import com.example.InventoryManagementSystem.security.JwtUtils;
import com.example.InventoryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @Override
    public Response registerUser(RegisterRequestDTO registerRequestDTO) {
        UserRole role = UserRole.MANAGER;

        if (registerRequestDTO.getRole() != null) {
            role = registerRequestDTO.getRole();
        }

        User userToSave = User.builder()
            .name(registerRequestDTO.getName())
            .email(registerRequestDTO.getEmail())
            .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
            .phoneNumber(registerRequestDTO.getPhoneNumber())
            .role(role)
            .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("user created successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not Found"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("password does not match");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("user logged in successfully")
                .role(user.getRole())
                .expirationTime("6 month")
                .token(token)
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

        userDTOS.forEach(userDTO -> userDTO.setTransactions(null));

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        user.setTransactions(null);
        return user;



    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("user not found"));

        if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if (userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPhoneNumber(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("user Successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));

        userRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("user Successfully Deleted")
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }
}
