package com.quickbite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI QuickBiteOpenAPI() {  // Creating OpenAPI bean (OpenAPI - A class provided by Swagger)
		
		// Setting manual fields because swagger needs general information about our API
		return new OpenAPI()
				.info(new io.swagger.v3.oas.models.info.Info()
	                .title("Quickbite - Practice")
	                .description("REST APIs for managing User")
	                .version("1.0.0") 
	                .contact(new Contact().name("Avinash Kumar")))  
				.components(new Components()
                        .addSecuritySchemes( 
                                "Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )  
                );
	}
}