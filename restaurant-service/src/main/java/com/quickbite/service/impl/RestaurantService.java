package com.quickbite.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickbite.dto.request.RestaurantRegisterRequestDto;
import com.quickbite.dto.request.RestaurantStatusRequestDto;
import com.quickbite.dto.request.RestaurantUpdateRequestDto;
import com.quickbite.dto.response.RestaurantAvailabilityResponseDto;
import com.quickbite.dto.response.RestaurantResponseDto;
import com.quickbite.exception.InvalidRestaurantOperationException;
import com.quickbite.exception.RestaurantNotFoundException;
import com.quickbite.exception.UnauthorizedRestaurantAccessException;
import com.quickbite.model.ApprovalStatus;
import com.quickbite.model.Restaurant;
import com.quickbite.repository.RestaurantRepository;
import com.quickbite.service.IRestaurantService;

@Service
public class RestaurantService implements IRestaurantService {

    private final RestaurantRepository restaurantRepository;

    // Constructor Injection
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    

    // --------- Restaurant Owner APIs ---------

    @Override
    @Transactional
    public RestaurantResponseDto registerRestaurant(Long ownerId, RestaurantRegisterRequestDto request) {

        Restaurant restaurant = Restaurant.builder()
					                 .ownerId(ownerId)
					                 .name(request.getName())
					                 .description(request.getDescription())
					                 .cuisine(request.getCuisine())
					                 .address(request.getAddress())
					                 .city(request.getCity())
					                 .phone(request.getPhone())
					                 .latitude(request.getLatitude())
					                 .longitude(request.getLongitude())
					                 .deliveryRadius(request.getDeliveryRadius())
					                 .minOrderAmount(request.getMinOrderAmount())
					                 .estimatedDeliveryMin(request.getEstimatedDeliveryMin())
					                 .isOpen(false)
					                 .approvalStatus(ApprovalStatus.PENDING)
					                 .createdAt(LocalDateTime.now())
					                 .build();

        
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return mapToResponse(savedRestaurant); 
    }
    

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> getMyRestaurants(Long ownerId) {

        return restaurantRepository.findByOwnerId(ownerId)
					               .stream()
					               .map(this::mapToResponse)
					               .toList();
    }
    

    @Override
    @Transactional
    public RestaurantResponseDto updateRestaurant(Long restaurantId, Long ownerId, RestaurantUpdateRequestDto request) {

        Restaurant restaurant = findRestaurant(restaurantId);

        // Check restaurant ownership
        validateOwner(restaurant, ownerId);

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setPhone(request.getPhone());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());
        restaurant.setDeliveryRadius(request.getDeliveryRadius());
        restaurant.setMinOrderAmount(request.getMinOrderAmount());
        restaurant.setEstimatedDeliveryMin(request.getEstimatedDeliveryMin());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return mapToResponse(updatedRestaurant); 
    }

    
    @Override
    @Transactional
    public RestaurantResponseDto updateOpenStatus(Long restaurantId, Long ownerId, RestaurantStatusRequestDto request) {

        Restaurant restaurant = findRestaurant(restaurantId);

        validateOwner(restaurant, ownerId);

        // Restaurant must be approved before opening
        if(Boolean.TRUE.equals(request.getOpen()) && restaurant.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new InvalidRestaurantOperationException("Restaurant must be approved before opening");
        }

        restaurant.setOpen(request.getOpen());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return mapToResponse(updatedRestaurant);
    }

    
    
    // --------- Admin APIs ----------

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> getPendingRestaurants() {

        return restaurantRepository.findByApprovalStatus(ApprovalStatus.PENDING)
					               .stream()
					               .map(this::mapToResponse)
					               .toList(); 
    }

    
    @Override
    @Transactional
    public RestaurantResponseDto approveRestaurant(Long restaurantId) {

        Restaurant restaurant = findRestaurant(restaurantId);

        if(restaurant.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new InvalidRestaurantOperationException("Only pending restaurants can be approved");
        }

        restaurant.setApprovalStatus(ApprovalStatus.APPROVED);

        Restaurant approvedRestaurant = restaurantRepository.save(restaurant);

        return mapToResponse(approvedRestaurant);
    }
 
    
    @Override
    @Transactional
    public RestaurantResponseDto rejectRestaurant(Long restaurantId) {

        Restaurant restaurant = findRestaurant(restaurantId);

        if(restaurant.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new InvalidRestaurantOperationException("Only pending restaurants can be rejected");
        }

        restaurant.setApprovalStatus(ApprovalStatus.REJECTED);
        restaurant.setOpen(false);

        Restaurant rejectedRestaurant = restaurantRepository.save(restaurant);

        return mapToResponse(rejectedRestaurant);
    }
    
    

    // --------- Public (Customer) APIs ----------

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> getAllRestaurants() {

        return restaurantRepository.findByApprovalStatus(ApprovalStatus.APPROVED)
				                   .stream()
				                   .map(this::mapToResponse)
				                   .toList();
    }

    
    @Override
    @Transactional(readOnly = true)
    public RestaurantResponseDto getRestaurantById(Long restaurantId) {

        Restaurant restaurant = findRestaurant(restaurantId);

        // Hide pending and rejected restaurants from customers
        if(restaurant.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId);
        }

        return mapToResponse(restaurant);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> searchRestaurants(String name) {

        return restaurantRepository.findByApprovalStatusAndNameContainingIgnoreCase(ApprovalStatus.APPROVED, name)
					               .stream()
					               .map(this::mapToResponse)
					               .toList();
    }
    

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> getRestaurantsByCity(String city) {

        return restaurantRepository.findByApprovalStatusAndCityIgnoreCase(ApprovalStatus.APPROVED, city)
					               .stream()
					               .map(this::mapToResponse)
					               .toList();
    }
    

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> getRestaurantsByCuisine(String cuisine) {

        return restaurantRepository.findByApprovalStatusAndCuisineIgnoreCase(ApprovalStatus.APPROVED, cuisine)
					               .stream()
					               .map(this::mapToResponse)
					               .toList();
    }
    

    
    // --------- Service Integration APIs -----------

    @Override
    @Transactional(readOnly = true)
    public RestaurantAvailabilityResponseDto checkAvailability(Long restaurantId) {

        Restaurant restaurant = findRestaurant(restaurantId);

        boolean acceptingOrders = restaurant.getApprovalStatus() == ApprovalStatus.APPROVED && restaurant.isOpen();

        return RestaurantAvailabilityResponseDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .approvalStatus(restaurant.getApprovalStatus())
                .open(restaurant.isOpen())
                .acceptingOrders(acceptingOrders)
                .build();
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public boolean isRestaurantOwnedBy(Long restaurantId, Long ownerId) {

        Restaurant restaurant = findRestaurant(restaurantId);

        return restaurant.getOwnerId().equals(ownerId);
    }
     
    

    // --------- Helper methods --------- 

    private Restaurant findRestaurant(Long restaurantId) {

        return restaurantRepository.findById(restaurantId)
                					  .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));
    } 
    

    private void validateOwner(Restaurant restaurant, Long ownerId) {

        if(!restaurant.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedRestaurantAccessException("You are not authorized to manage this restaurant");
        } 
    }

    
    private RestaurantResponseDto mapToResponse(Restaurant restaurant) {

        return RestaurantResponseDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .ownerId(restaurant.getOwnerId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .cuisine(restaurant.getCuisine())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .phone(restaurant.getPhone())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .deliveryRadius(restaurant.getDeliveryRadius())
                .minOrderAmount(restaurant.getMinOrderAmount()) 
                .estimatedDeliveryMin(restaurant.getEstimatedDeliveryMin())
                .open(restaurant.isOpen())
                .approvalStatus(restaurant.getApprovalStatus())
                .createdAt(restaurant.getCreatedAt()) 
                .build();
    }
}