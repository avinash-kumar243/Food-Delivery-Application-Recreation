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

// Owners should be able to edit restaurant details, but not approval status or ownership.

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateRequestDto {

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

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotNull
    @Positive
    private Double deliveryRadius;

    @NotNull
    @PositiveOrZero
    private BigDecimal minOrderAmount;

    @NotNull
    @Positive
    private Integer estimatedDeliveryMin;
}
