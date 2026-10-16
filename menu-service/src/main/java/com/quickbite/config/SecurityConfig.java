package com.quickbite.config;

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

import com.quickbite.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // Swagger
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**")
                .permitAll()

                // H2 Console
                .requestMatchers("/h2-console/**")
                .permitAll()

                // Actuator
                .requestMatchers(
                        "/actuator/health",
                        "/actuator/health/**",
                        "/actuator/info")
                .permitAll()

                // Internal APIs: JWT required
                .requestMatchers("/api/v1/internal/menu/**")
                .authenticated()

                // Public menu browsing
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/menu/restaurants/**",
                        "/api/v1/menu/items/*")
                .permitAll()

                // Restaurant owner management
                .requestMatchers(HttpMethod.POST,
                        "/api/v1/menu/categories",
                        "/api/v1/menu/items")
                .hasRole("RESTAURANT_OWNER")

                .requestMatchers(HttpMethod.PUT,
                        "/api/v1/menu/categories/*",
                        "/api/v1/menu/items/*")
                .hasRole("RESTAURANT_OWNER")

                .requestMatchers(HttpMethod.DELETE,
                        "/api/v1/menu/categories/*",
                        "/api/v1/menu/items/*")
                .hasRole("RESTAURANT_OWNER")

                .requestMatchers(HttpMethod.PATCH,
                        "/api/v1/menu/items/*/availability")
                .hasRole("RESTAURANT_OWNER")

                .anyRequest().authenticated() 
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, exception) ->
                        writeError(response, HttpStatus.UNAUTHORIZED, "Authentication is required"))

                .accessDeniedHandler((request, response, exception) ->
                        writeError(response, HttpStatus.FORBIDDEN, "Access denied"))
            )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws java.io.IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = """
                {
                  "status": %d,
                  "message": "%s"
                }
                """.formatted(status.value(), message);

        response.getWriter().write(json);
    }
}
