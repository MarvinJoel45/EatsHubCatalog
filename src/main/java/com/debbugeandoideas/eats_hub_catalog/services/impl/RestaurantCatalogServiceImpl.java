package com.debbugeandoideas.eats_hub_catalog.services.impl;

import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import com.debbugeandoideas.eats_hub_catalog.records.Address;
import com.debbugeandoideas.eats_hub_catalog.repositories.RestaurantRepository;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantCatalogServiceImpl implements RestaurantCatalogService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Flux<RestaurantCollection> readAll(Integer page, Integer size) {
        return this.restaurantRepository.findAll()
                .skip((long) page *size)
                .take(size);
    }

    @Override
    public Flux<RestaurantCollection> readByCuisineType(String cuisineType) {
        return this.restaurantRepository.findByCuisineType(cuisineType)
                .doOnSubscribe(subscription -> log.info("Init search with param: {}", cuisineType))
                .doOnNext(restaurant -> log.info("Found with param: {}", restaurant.getName()))
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage(),throwable);
                    return Flux.empty();
                });
    }

    @Override
    public Mono<RestaurantCollection> readByName(String name) {
        return this.restaurantRepository.findByNameStartingWithIgnoreCase(name)
                .doOnSubscribe(subscription -> log.info("Init search start with param: {}", name))
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage(),throwable);
                    return Mono.empty();
                });
    }

    @Override
    public Flux<RestaurantCollection> readByPriceRangeIn(List<PriceEnum> priceRanges) {
        return this.restaurantRepository.findByPriceRangeIn(priceRanges)
                .switchIfEmpty(Flux.empty().cast(RestaurantCollection.class)
                        .doOnSubscribe(s -> log.info("Restaurant is empty")));
    }

    @Override
    public Flux<RestaurantCollection> readByCity(String city) {
        return this.restaurantRepository.findAll()
                .map(RestaurantCollection::getAddress)
                .filter(Objects::nonNull)
                .map(Address::city)
                .filter(Objects::nonNull)
                .distinct()
                .collectList()
                .flatMapMany(cities -> {

                    if (cities.isEmpty()) {
                        log.info("No resturants found in city: {}",city);
                        return Flux.empty();
                    }

                    log.info("Init search in city: {}", city);
                    return this.restaurantRepository.findByAddressCity(city)
                            .doOnNext(restaurant ->
                                    log.info("Found restaurant in city: {} with param: {}", city,restaurant.getName()));
                })
                .onErrorResume(throwable -> {
                    log.error(throwable.getMessage(),throwable);
                    return Flux.empty();
                });
    }
}
