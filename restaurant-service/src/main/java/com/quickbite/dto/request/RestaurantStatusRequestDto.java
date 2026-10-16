package com.quickbite.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This DTO handles opening and closing restaurants

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantStatusRequestDto {

    @NotNull(message = "Open status is required")
    private Boolean open;
}