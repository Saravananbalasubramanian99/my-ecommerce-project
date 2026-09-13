package com.ecommerce.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByVerificationToken(String verificationToken);

    boolean existsByEmail(String email);
}