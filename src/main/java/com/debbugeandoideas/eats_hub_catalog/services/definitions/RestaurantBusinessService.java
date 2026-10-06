package com.debbugeandoideas.eats_hub_catalog.services.definitions;

import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantBusinessService {

    Flux<RestaurantResponse> readAll(Integer page, Integer size);

    Flux<RestaurantResponse> readByCuisineType(String cuisineType);

    Mono<RestaurantResponse> readByName(String name);

    Flux<RestaurantResponse> readByPriceRangeIn(List<PriceEnum> priceRanges);

    Flux<RestaurantResponse> readByCity(String city);
}
