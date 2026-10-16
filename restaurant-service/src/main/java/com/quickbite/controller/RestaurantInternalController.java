package com.quickbite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.quickbite.dto.response.RestaurantAvailabilityResponseDto;
import com.quickbite.security.AuthenticatedUser;
import com.quickbite.service.IRestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// 


@RestController
@RequestMapping("/api/v1/internal/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant Integration APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class RestaurantInternalController {

    private final IRestaurantService restaurantService;

    @GetMapping("/{restaurantId}/availability")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'RESTAURANT_OWNER', 'DELIVERY_PARTNER', 'ADMIN')")
    @Operation(summary = "Check restaurant availability")
    public ResponseEntity<RestaurantAvailabilityResponseDto> checkAvailability(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                restaurantService.checkAvailability(restaurantId));
    }

    @GetMapping("/{restaurantId}/ownership")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Verify current owner owns restaurant")
    public ResponseEntity<Boolean> checkOwnership(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal AuthenticatedUser user) {

        boolean isOwner = restaurantService.isRestaurantOwnedBy(
                restaurantId, user.userId());

        return ResponseEntity.ok(isOwner);
    }
}
