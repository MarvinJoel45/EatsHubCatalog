package com.debbugeandoideas.eats_hub_catalog.services.impl;

import com.debbugeandoideas.eats_hub_catalog.dtos.Review;
import com.debbugeandoideas.eats_hub_catalog.dtos.events.ReviewEvent;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewStreamService {

    private final ReviewService reviewService;

    public Mono<Object> processReview(ReviewEvent reviewEvent) {
        return Mono.fromRunnable(() -> log.info("Processing review {}",reviewEvent))
                .then(Mono.defer(() -> {
                    try {

                        Review review = Review.builder()
                                .customerId(reviewEvent.getUuidCustomer())
                                .customerName(reviewEvent.getUuidCustomer())
                                .rating(reviewEvent.getRating())
                                .timestamp(Instant.ofEpochMilli(reviewEvent.getTimestamp()))
                                .comment(reviewEvent.getComment())
                                .build();

                        UUID idRestaurant = UUID.fromString(reviewEvent.getIdRestaurant());

                        log.info("Adding review for restaurant {} from event",idRestaurant);

                        return this.reviewService.addRestaurantReview(idRestaurant,review)
                                .doOnSuccess(res -> log.info("Review added to restaurant {}",idRestaurant));
                    } catch (IllegalArgumentException ex) {
                        return Mono.error(ex);
                    }
                }));
    }
}
