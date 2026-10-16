package com.quickbite.service;

import java.util.List;

import com.quickbite.dto.request.RestaurantRegisterRequestDto;
import com.quickbite.dto.request.RestaurantStatusRequestDto;
import com.quickbite.dto.request.RestaurantUpdateRequestDto;
import com.quickbite.dto.response.RestaurantAvailabilityResponseDto;
import com.quickbite.dto.response.RestaurantResponseDto;

public interface IRestaurantService {

    // Restaurant Owner
    RestaurantResponseDto registerRestaurant(Long ownerId, RestaurantRegisterRequestDto request);
    List<RestaurantResponseDto> getMyRestaurants(Long ownerId);
    RestaurantResponseDto updateRestaurant(Long restaurantId, Long ownerId, RestaurantUpdateRequestDto request);
    RestaurantResponseDto updateOpenStatus(Long restaurantId, Long ownerId, RestaurantStatusRequestDto request);

    // Admin
    List<RestaurantResponseDto> getPendingRestaurants();
    RestaurantResponseDto approveRestaurant(Long restaurantId);
    RestaurantResponseDto rejectRestaurant(Long restaurantId); 

    // Customer / Public
    List<RestaurantResponseDto> getAllRestaurants();
    RestaurantResponseDto getRestaurantById(Long restaurantId);
    List<RestaurantResponseDto> searchRestaurants(String name); 
    List<RestaurantResponseDto> getRestaurantsByCity(String city);
    List<RestaurantResponseDto> getRestaurantsByCuisine(String cuisine);

    // Integration with other services
    RestaurantAvailabilityResponseDto checkAvailability(Long restaurantId);
    boolean isRestaurantOwnedBy(Long restaurantId, Long ownerId);
}