package com.debbugeandoideas.eats_hub_catalog.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEvent {

    private String idRestaurant;
    private Integer rating;
    private String uuidCustomer;
    private String comment;
    private String eventType;

    @Builder.Default
    private Long timestamp = System.currentTimeMillis();
}
