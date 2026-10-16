package com.quickbite.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quickbite.dto.response.ErrorResponseDto;
import com.quickbite.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;
 
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            .authorizeHttpRequests(auth -> auth

                    // Swagger
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**"
                    ).permitAll()

                    // H2 Console
                    .requestMatchers("/h2-console/**").permitAll()

                    // Actuator health and info
                    .requestMatchers(
                            "/actuator/health",
                            "/actuator/health/**",
                            "/actuator/info"
                    ).permitAll()

                    // Admin APIs
                    .requestMatchers("/api/v1/admin/restaurants/**")
                    .hasRole("ADMIN")

                    // Integration APIs
                    .requestMatchers("/api/v1/internal/restaurants/**")
                    .authenticated()

                    // Owner-specific GET
                    .requestMatchers(HttpMethod.GET, "/api/v1/restaurants/my")
                    .hasRole("RESTAURANT_OWNER")
                    
                    // Public restaurant browsing
                    .requestMatchers(HttpMethod.GET, 
                            "/api/v1/restaurants",
                            "/api/v1/restaurants/search",
                            "/api/v1/restaurants/city/**",
                            "/api/v1/restaurants/cuisine/**",
                            "/api/v1/restaurants/{restaurantId:[0-9]+}"
                    ).permitAll()

                    // Owner registration
                    .requestMatchers(HttpMethod.POST, "/api/v1/restaurants")
                    .hasRole("RESTAURANT_OWNER")
                    
                    // Owner updates
                    .requestMatchers(HttpMethod.PUT, "/api/v1/restaurants/*")
                    .hasRole("RESTAURANT_OWNER")

                    .requestMatchers(HttpMethod.PATCH, "/api/v1/restaurants/*/open-status")
                    .hasRole("RESTAURANT_OWNER")

                    .anyRequest().authenticated() 
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, e) -> writeError(response, HttpStatus.UNAUTHORIZED, "Authentication is required"))
                    .accessDeniedHandler((request, response, e) -> writeError(response, HttpStatus.FORBIDDEN, "Access denied"))
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); 
    }
     
    
    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws java.io.IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDto error = ErrorResponseDto.builder()
								                .status(status.value())
								                .message(message)
								                .timestamp(LocalDateTime.now())
								                .build();

        objectMapper.writeValue(response.getOutputStream(), error);
    }
}