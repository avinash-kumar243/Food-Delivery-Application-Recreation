package com.quickbite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quickbite.dto.response.RestaurantResponseDto;
import com.quickbite.service.IRestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// These are public APIs for customers to discover approved restaurants

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Public Restaurant APIs")
public class RestaurantController {

    private final IRestaurantService restaurantService;
    

    @GetMapping
    @Operation(summary = "Get all approved restaurants")
    public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants() {

        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    
    @GetMapping("/search")
    @Operation(summary = "Search restaurants by name")
    public ResponseEntity<List<RestaurantResponseDto>> searchRestaurants(@RequestParam String name) {

        return ResponseEntity.ok(restaurantService.searchRestaurants(name));
    }
    

    @GetMapping("/city/{city}")
    @Operation(summary = "Get restaurants by city")
    public ResponseEntity<List<RestaurantResponseDto>> getByCity(@PathVariable String city) {

        return ResponseEntity.ok(restaurantService.getRestaurantsByCity(city));
    }
    

    @GetMapping("/cuisine/{cuisine}")
    @Operation(summary = "Get restaurants by cuisine")
    public ResponseEntity<List<RestaurantResponseDto>> getByCuisine(@PathVariable String cuisine) {

        return ResponseEntity.ok(restaurantService.getRestaurantsByCuisine(cuisine));
    }
     

    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get approved restaurant details")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long restaurantId) {

        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }
}