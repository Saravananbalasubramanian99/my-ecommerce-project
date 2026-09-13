package com.ecommerce.backend.security;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.ecommerce.backend.security.OAuth2AuthenticationSuccessHandler;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, authException) -> {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"status\":401,\"message\":\"Authentication required\"}"
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
        	.csrf(csrf -> csrf.disable())			// needed only for session‑based authentication.
        	
        	.cors(cors -> {})						//Your frontend (React/Angular/Vue) runs on a different port, Without CORS, browser blocks requests.

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS	//Make the app stateless
                )
            )
            
            .oauth2Login(oauth2 ->
	            oauth2.successHandler(oauth2AuthenticationSuccessHandler)
	        )

            .authorizeHttpRequests(auth -> auth		//This block defines who can access what.

                .requestMatchers(
                    "/api/auth/**"
                ).permitAll()

                .requestMatchers(
                	    org.springframework.http.HttpMethod.GET,
                	    "/api/products/**",
                	    "/api/categories/**"
                	).permitAll()

                	.requestMatchers(
                	    org.springframework.http.HttpMethod.POST,
                	    "/api/products/**",
                	    "/api/categories/**"
                	).hasAuthority("ADMIN")

                	.requestMatchers(
                	    org.springframework.http.HttpMethod.PUT,
                	    "/api/products/**",
                	    "/api/categories/**"
                	).hasAuthority("ADMIN")

                	.requestMatchers(
                	    org.springframework.http.HttpMethod.DELETE,
                	    "/api/products/**",
                	    "/api/categories/**"
                	).hasAuthority("ADMIN")

                	.anyRequest().authenticated()			//User must have a valid JWT token
            )

            .exceptionHandling(exception ->					//Missing token, Invalid token, Expired token
                exception.authenticationEntryPoint(
                    authenticationEntryPoint()
                )
            )

            .addFilterBefore(				//Before you try to authenticate using username/password,first run my custom JWT filter.
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of("http://localhost:4200")
        );

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}








//package com.ecommerce.backend.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                        "/api/auth/**",
//                        "/api/categories/**",
//                        "/api/products/**"
//                ).permitAll()
//                .anyRequest().authenticated()
//            );
//
//        return http.build();
//    }
//}



//package com.ecommerce.backend.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {		//This configures Spring Security's HTTP security rules.
//
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll()				//Allow every HTTP request without authentication.
//            );
//
//        return http.build();
//    }
//}