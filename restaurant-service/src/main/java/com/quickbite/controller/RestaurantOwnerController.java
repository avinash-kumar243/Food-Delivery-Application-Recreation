package com.quickbite.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.quickbite.dto.request.RestaurantRegisterRequestDto;
import com.quickbite.dto.request.RestaurantStatusRequestDto;
import com.quickbite.dto.request.RestaurantUpdateRequestDto;
import com.quickbite.dto.response.RestaurantResponseDto;
import com.quickbite.security.AuthenticatedUser;
import com.quickbite.service.IRestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// This controller contains the four APIs used by restaurant owners

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant Owner APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class RestaurantOwnerController {

    private final IRestaurantService restaurantService;
    

    @PostMapping 
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Register a new restaurant")
    public ResponseEntity<RestaurantResponseDto> registerRestaurant(@Valid @RequestBody RestaurantRegisterRequestDto request, @AuthenticationPrincipal AuthenticatedUser user) {

        RestaurantResponseDto response = restaurantService.registerRestaurant(user.userId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    
    @GetMapping("/my")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Get all restaurants owned by current user")
    public ResponseEntity<List<RestaurantResponseDto>> getMyRestaurants(@AuthenticationPrincipal AuthenticatedUser user) {

        return ResponseEntity.ok(restaurantService.getMyRestaurants(user.userId()));
    }
    

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Update own restaurant")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable Long restaurantId, @Valid @RequestBody RestaurantUpdateRequestDto request, @AuthenticationPrincipal AuthenticatedUser user) {

        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantId, user.userId(), request));
    } 
    

    @PatchMapping("/{restaurantId}/open-status")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Open or close own restaurant")
    public ResponseEntity<RestaurantResponseDto> updateOpenStatus(@PathVariable Long restaurantId, @Valid @RequestBody RestaurantStatusRequestDto request, @AuthenticationPrincipal AuthenticatedUser user) {

        return ResponseEntity.ok(restaurantService.updateOpenStatus(restaurantId, user.userId(), request));
    }
} 