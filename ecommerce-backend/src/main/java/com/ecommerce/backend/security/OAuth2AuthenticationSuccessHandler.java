package com.ecommerce.backend.security;

import com.ecommerce.backend.entity.Role;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.security.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2AuthenticationSuccessHandler(
            UserRepository userRepository,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        // Google unique ID
        String googleId = oidcUser.getSubject();

        // Google account information
        String email = oidcUser.getEmail();
        String firstName = oidcUser.getGivenName();
        String lastName = oidcUser.getFamilyName();

        if (firstName == null || firstName.isBlank()) {
            firstName = oidcUser.getFullName();
        }

        if (lastName == null) {
            lastName = "";
        }

        // Find existing user by Google ID
        User user = userRepository.findByGoogleId(googleId)
                .orElse(null);

        // If Google ID doesn't exist, check email
        if (user == null) {

            user = userRepository.findByEmail(email)
                    .orElse(null);
        }

        // Existing user
        if (user != null) {

            // Connect Google account to existing account
            user.setGoogleId(googleId);

            // Google has already verified the email
            user.setEmailVerified(true);

            userRepository.save(user);

        } else {

            // Create a new user
            user = new User();

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setGoogleId(googleId);

            // No password required for Google login
            user.setPassword(null);

            user.setEmailVerified(true);
            user.setRole(Role.USER);

            userRepository.save(user);
        }

        // Generate our application's JWT
        String token = jwtService.generateToken(user);

        // Send JWT to Angular
        String redirectUrl =
                "http://localhost:4200/oauth-success"
                + "#token=" + token
                + "&userId=" + user.getId()
                + "&firstName=" + user.getFirstName()
                + "&email=" + user.getEmail()
                + "&role=" + user.getRole();

        response.sendRedirect(redirectUrl);
    }
}