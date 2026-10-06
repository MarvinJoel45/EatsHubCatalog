package com.debbugeandoideas.eats_hub_catalog.repositories;

import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantCollection, UUID> {

    Flux<RestaurantCollection> findByCuisineType(String cuisineType);

    Mono<RestaurantCollection> findByNameStartingWithIgnoreCase(String name);

//    @Query("{'name': {$regex:'^?0', $options: '¡'}}")
//    Flux<RestaurantCollection> findByName(String name);

    Flux<RestaurantCollection> findByPriceRangeIn(List<PriceEnum> priceRanges);

    Flux<RestaurantCollection> findByAddressCity(String city);
}
