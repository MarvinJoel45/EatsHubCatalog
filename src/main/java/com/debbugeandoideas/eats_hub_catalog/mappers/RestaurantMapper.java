package com.debbugeandoideas.eats_hub_catalog.mappers;

import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.dtos.Review;
import com.debbugeandoideas.eats_hub_catalog.dtos.responses.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "globalRating", expression = "java(calculateGlobalRating(collection.getReviews()))")
    RestaurantResponse toResponse(RestaurantCollection collection);

    default Flux<RestaurantResponse> toResponseFlux(Flux<RestaurantCollection> collections) {
        return collections.map(this::toResponse);
    }

    default Mono<RestaurantResponse> toResponseMono(Mono<RestaurantCollection> collection) {
        return collection.map(this::toResponse);
    }

    default Double calculateGlobalRating(List<Review> reviews) {
        if (Objects.isNull(reviews) || reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .filter(review -> Objects.nonNull(review.getRating()))
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
