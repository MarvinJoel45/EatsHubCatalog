package com.debbugeandoideas.eats_hub_catalog.dtos.responses;

import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import com.debbugeandoideas.eats_hub_catalog.records.Address;
import com.debbugeandoideas.eats_hub_catalog.records.ContactInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantResponse {

    private String name;
    private Address address;
    private String cuisineType;
    private PriceEnum priceRange;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private ContactInfo contactInfo;
    private Double globalRating;
}
