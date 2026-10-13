package com.debbugeandoideas.eats_hub_catalog.services.impl;

import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.dtos.Review;
import com.debbugeandoideas.eats_hub_catalog.repositories.RestaurantRepository;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Mono<RestaurantCollection> addRestaurantReview(UUID idRestaurant, Review review) {
        log.info("Adding review for resturant: {}", idRestaurant);

        return this.restaurantRepository.findById(idRestaurant)
                .switchIfEmpty(Mono.error(new RuntimeException("Restaurant not found")))
                .flatMap(restaurantDB -> {

                    if (Objects.isNull(restaurantDB.getReviews())) {
                        restaurantDB.setReviews(new ArrayList<>());
                    }

                    restaurantDB.getReviews().add(review);

                    log.info("Restaurant review {} added", idRestaurant);

                    return this.restaurantRepository.save(restaurantDB);
                })
                .doOnSuccess(restaurant -> log.info("Restaurant {} updated success",idRestaurant))
                .doOnError(throwable -> log.error("Restaurant {} error",idRestaurant,throwable));
    }
}
