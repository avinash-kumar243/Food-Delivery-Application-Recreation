
package com.quickbite.dto.response;

import com.quickbite.model.ApprovalStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Order Service and Cart Service, will need to know whether a restaurant currently accept orders

@Getter
@Setter
@Builder
public class RestaurantAvailabilityResponseDto {

    private Long restaurantId;

    private ApprovalStatus approvalStatus;

    private boolean open;

    private boolean acceptingOrders;
}