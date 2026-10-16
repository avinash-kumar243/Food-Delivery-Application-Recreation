package com.quickbite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quickbite.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
		
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}


	@Bean 
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
						"/auth/v1/auth/login", 
						"/auth/v1/auth/register",
						"/auth/v1/auth/forgot-password",
						"/auth/v1/auth/reset-password"
				).permitAll()
				
				// Public Restaurant APIs
		        .requestMatchers(HttpMethod.GET, 
		                "/api/v1/restaurants",
		                "/api/v1/restaurants/*",
		                "/api/v1/restaurants/search",
		                "/api/v1/restaurants/city/**",
		                "/api/v1/restaurants/cuisine/**"
		        ).permitAll()
		        
				.requestMatchers(
						"/actuator/health",
						"/actuator/info"
				).permitAll()
				
				.anyRequest().authenticated()
			)
			
			// Add our JWT filter before UsernamePasswordAuthenticationFilter
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build(); 
	}
}