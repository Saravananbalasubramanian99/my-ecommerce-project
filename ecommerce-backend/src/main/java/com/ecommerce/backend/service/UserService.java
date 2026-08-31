package com.ecommerce.backend.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.dto.UserRequestDTO;
import com.ecommerce.backend.dto.UserResponseDTO;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.entity.Role;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.repository.UserRepository;

import com.ecommerce.backend.dto.LoginRequestDTO;
import com.ecommerce.backend.dto.LoginResponseDTO;
import com.ecommerce.backend.security.JwtService;

import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ForbiddenException;
import com.ecommerce.backend.exception.UnauthorizedException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    // Register a new user
    public UserResponseDTO registerUser(
            UserRequestDTO request) {

        // Check whether email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email is already registered");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Hash password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // New users must verify their email
        user.setEmailVerified(false);

        // Generate verification token
        user.setVerificationToken(
                UUID.randomUUID().toString()
        );
        
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getVerificationToken()
        );

        return convertToResponseDTO(savedUser);
    }
    
    public void verifyEmail(String token) {

        User user = userRepository
                .findByVerificationToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid verification token"));

        user.setEmailVerified(true);

        user.setVerificationToken(null);

        userRepository.save(user);
    }
    
    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid email or password"));

        if (!user.isEmailVerified()) {

            throw new ForbiddenException(
                    "Please verify your email before logging in");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new UnauthorizedException(
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(
                token,
                user.getId(),
                user.getFirstName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    // Convert Entity → Response DTO
    private UserResponseDTO convertToResponseDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isEmailVerified()
        );
    }
}