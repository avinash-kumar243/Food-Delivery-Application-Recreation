package com.quickbite.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	
	public JwtAuthenticationFilter(JwtUtil jwtUtil) { 
		this.jwtUtil = jwtUtil;
	} 


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		 // 1. Get Authorization header
		String authorizationHeader = request.getHeader("Authorization");
		
		// 2. No JWT → continue
        // Spring Security will decide whether this endpoint is public or protected
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        } 
        
        // 3. Extract JWT
        String token = authorizationHeader.substring(7);
                
        try {
        	
        		// 4. Validate JWT
            if(jwtUtil.isTokenValid(token)) {

                // 5. Extract user ID from JWT
                Long userId = jwtUtil.extractUserId(token); 

                // 6. Create Authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
 
                // 7. Store authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication); 
            }

        	
        } catch(Exception e) {
        	
        		// Invalid JWT
            SecurityContextHolder.clearContext();
        }
        
        // 8. Continue request
        filterChain.doFilter(request, response); 
	}
	
	
	
//							    HTTP REQUEST
//							    │
//							    ▼
//							JwtAuthenticationFilter
//							    │
//							    ▼
//							JwtUtil
//							/        \
//							/          \
//							Valid JWT?       User ID
//							│              │
//							▼              ▼
//							YES          Authentication
//							│              │
//							└──────┬───────┘
//							  ▼
//							SecurityContextHolder
//							  │
//							  ▼
//							SecurityConfig
//							/           \
//							PUBLIC       PROTECTED
//							│              │
//							▼              ▼
//							ALLOW       authenticated?
//							         │
//							    YES /    \ NO
//							       ▼       ▼
//							     ALLOW    401

}