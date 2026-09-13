package com.ecommerce.backend.security;

import java.nio.charset.StandardCharsets;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1 hour

    private SecretKey getSigningKey() {							//converting the string into a proper HMAC SHA key.

        return Keys.hmacShaKeyFor(								//This method takes your byte array and turns it into a SecretKey object.	
        		secretKey.getBytes(StandardCharsets.UTF_8)	 	//This converts the string into a byte array
        );
    }

    public String generateToken(User user) {

        Date now = new Date();

        Date expiration =
                new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()									//This line in your code creates the token
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();										//This converts everything into the final JWT string
    }

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();							//In JWT, the subject (sub) field usually stores the user’s email or username.
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()							//This parser is responsible for reading and validating JWT tokens.
                .verifyWith(getSigningKey())			//Use this secret key to verify the token’s signature.
                .build()
                .parseSignedClaims(token)
                .getPayload();							//This extracts the payload, which contains all the claims.
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String email = extractEmail(token);

        return email.equals(userDetails.getUsername())	//Does the email inside the token match the email of the user trying to authenticate?
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}