package com.ecommerce.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.backend.dto.UserRequestDTO;
import com.ecommerce.backend.dto.UserResponseDTO;
import com.ecommerce.backend.service.UserService;

import com.ecommerce.backend.dto.LoginRequestDTO;
import com.ecommerce.backend.dto.LoginResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO request) {

        UserResponseDTO user =
                userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
    
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token) {

        userService.verifyEmail(token);

        return ResponseEntity.ok(
                "Email verified successfully. "
                + "You can now log in."
        );
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response =
                userService.login(request);

        return ResponseEntity.ok(response);
    }
}