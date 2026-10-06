package com.debbugeandoideas.eats_hub_catalog.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Object customerId;
    private String customerName;
    private Integer rating;
    private String comment;
    private Instant timestamp;
}
