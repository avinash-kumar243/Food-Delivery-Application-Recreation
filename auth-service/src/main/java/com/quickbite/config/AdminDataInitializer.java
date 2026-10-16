package com.quickbite.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.quickbite.model.Role;
import com.quickbite.model.User;
import com.quickbite.repository.UserRepository;


@Configuration
@Profile("dev")
public class AdminDataInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            if(!userRepository.existsByEmail(adminEmail)) {

                User admin = User.builder()
                        .username("QuickBite Admin")
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .phone("9999999999")
                        .role(Role.ADMIN)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(admin);

                System.out.println("Default ADMIN account created");
            }
        };
    }
}