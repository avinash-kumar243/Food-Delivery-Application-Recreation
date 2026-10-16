package com.quickbite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quickbite.model.ApprovalStatus;
import com.quickbite.model.Restaurant;

// Spring Data JPA automatically generates queries from the method names

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // One owner can have multiple restaurants
    List<Restaurant> findByOwnerId(Long ownerId);

    // Restaurants waits for admin approval
    List<Restaurant> findByApprovalStatus(ApprovalStatus approvalStatus);

    // Public restaurant search
    List<Restaurant> findByApprovalStatusAndNameContainingIgnoreCase(ApprovalStatus approvalStatus, String name);

    // Filter by city
    List<Restaurant> findByApprovalStatusAndCityIgnoreCase(ApprovalStatus approvalStatus, String city);

    // Filter by cuisine 
    List<Restaurant> findByApprovalStatusAndCuisineIgnoreCase(ApprovalStatus approvalStatus, String cuisine);
}