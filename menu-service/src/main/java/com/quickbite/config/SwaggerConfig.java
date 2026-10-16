package com.quickbite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	
    @Bean
    public OpenAPI menuServiceOpenAPI() {

        return new OpenAPI().info(new Info() 
                        		.title("QuickBite - Menu Service")
                        		.description("REST APIs for managing restaurant menus")
                        		.version("1.0.0"))

                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}