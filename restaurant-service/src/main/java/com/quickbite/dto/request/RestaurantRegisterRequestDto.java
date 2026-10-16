package com.quickbite.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// It contains only those fields that the restaurant owner can submit to register a restaurant

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegisterRequestDto {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String description;

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15)
    private String phone;

    @NotNull(message = "Latitude is required")
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotNull(message = "Delivery radius is required")
    @Positive(message = "Delivery radius must be positive")
    private Double deliveryRadius;

    @NotNull(message = "Minimum order amount is required")
    @PositiveOrZero
    private BigDecimal minOrderAmount;

    @NotNull(message = "Estimated delivery time is required")
    @Positive
    private Integer estimatedDeliveryMin;
}
