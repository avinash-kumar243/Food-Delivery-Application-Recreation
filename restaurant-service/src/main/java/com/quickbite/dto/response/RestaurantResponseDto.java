package com.quickbite.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.quickbite.model.ApprovalStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RestaurantResponseDto {

    private Long restaurantId;

    private Long ownerId;

    private String name;

    private String description;

    private String cuisine;

    private String address;

    private String city;

    private String phone;

    private Double latitude;

    private Double longitude;

    private Double deliveryRadius;

    private BigDecimal minOrderAmount;

    private Integer estimatedDeliveryMin;

    private boolean open;

    private ApprovalStatus approvalStatus;

    private LocalDateTime createdAt;
}
