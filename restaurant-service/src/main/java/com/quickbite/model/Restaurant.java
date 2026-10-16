package com.quickbite.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "restaurants") 
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    // User ID from Auth Service
    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String name;

    private String description;

    private String cuisine;

    private String address;

    private String city;

    private String phone;

    private Double latitude;

    private Double longitude;

    // Delivery radius in kilometers
    private Double deliveryRadius;

    @Column(precision = 12, scale = 2)
    private BigDecimal minOrderAmount;

    private Integer estimatedDeliveryMin;

    // Restaurant accepting orders or not
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

    private LocalDateTime createdAt;
}
