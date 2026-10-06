package com.debbugeandoideas.eats_hub_catalog.services.definitions;

import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantCatalogService  {

    Flux<RestaurantCollection> readAll(Integer page, Integer size);

    Flux<RestaurantCollection> readByCuisineType(String cuisineType);

    Mono<RestaurantCollection> readByName(String name);

    Flux<RestaurantCollection> readByPriceRangeIn(List<PriceEnum> priceRanges);

    Flux<RestaurantCollection> readByCity(String city);
}
