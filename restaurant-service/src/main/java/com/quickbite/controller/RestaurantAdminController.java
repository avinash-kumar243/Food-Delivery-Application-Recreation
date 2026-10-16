package com.quickbite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.quickbite.dto.response.RestaurantResponseDto;
import com.quickbite.service.IRestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// Only users with the ADMIN role can access these APIs

@RestController
@RequestMapping("/api/v1/admin/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant Admin APIs")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class RestaurantAdminController {

    private final IRestaurantService restaurantService;
    

    @GetMapping("/pending")
    @Operation(summary = "Get restaurants waiting for approval")
    public ResponseEntity<List<RestaurantResponseDto>> getPendingRestaurants() {

        return ResponseEntity.ok(restaurantService.getPendingRestaurants());
    }
    

    @PatchMapping("/{restaurantId}/approve")
    @Operation(summary = "Approve a pending restaurant")
    public ResponseEntity<RestaurantResponseDto> approveRestaurant(@PathVariable Long restaurantId) {

        return ResponseEntity.ok(restaurantService.approveRestaurant(restaurantId));
    }
    

    @PatchMapping("/{restaurantId}/reject")
    @Operation(summary = "Reject a pending restaurant")
    public ResponseEntity<RestaurantResponseDto> rejectRestaurant(@PathVariable Long restaurantId) {

        return ResponseEntity.ok(restaurantService.rejectRestaurant(restaurantId));
    } 
}