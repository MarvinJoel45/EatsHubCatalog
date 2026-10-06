package com.debbugeandoideas.eats_hub_catalog.services.impl;

import com.debbugeandoideas.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import com.debbugeandoideas.eats_hub_catalog.mappers.RestaurantMapper;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.RestaurantBusinessService;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.RestaurantCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantBusinessServiceImpl implements RestaurantBusinessService {

    private final RestaurantCatalogService restaurantCatalogService;
    private final RestaurantMapper restaurantMapper;
    private final CatalogCacheService restaurantCache;

    @Override
    public Flux<RestaurantResponse> readAll(Integer page, Integer size) {
        log.info("Reading page:{}, size: {}",page,size);

        return this.restaurantCatalogService
                .readAll(page,size)
                .transform(this.restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading all restaurants completed"));
    }

    @Override
    public Flux<RestaurantResponse> readByCuisineType(String cuisineType) {
        log.info("Reading restaurants by cuisine type: {}", cuisineType);

        final String cacheKey = CatalogCacheService.buildCuisineTypeKey(cuisineType);

        return this.restaurantCache.getCacheRestaurants(cacheKey)
                    .switchIfEmpty(
                        this.restaurantCatalogService
                                .readByCuisineType(cuisineType)
                                .transform(this.restaurantMapper::toResponseFlux)
                                .transform(restaurantsDB -> this.restaurantCache.cacheRestaurants(cacheKey,restaurantsDB))

                    )
                    .doOnComplete(() -> log.info("Reading restaurants by cuisine type completed"));
    }

    @Override
    public Mono<RestaurantResponse> readByName(String name) {
        log.info("Reading restaurant by name: {}", name);

        final String cacheKey = CatalogCacheService.buildNameKey(name);

        return this.restaurantCache.getCacheRestaurant(cacheKey)
                    .switchIfEmpty(
                            this.restaurantCatalogService
                                    .readByName(name)
                                    .transform(this.restaurantMapper::toResponseMono)
                                    .flatMap(restaurantDB -> this.restaurantCache.cacheRestaurant(cacheKey,restaurantDB))
                    )
                    .doOnSuccess(restaurant -> {
                        if (Objects.isNull(restaurant)) {
                            log.info("Reading restaurant by name completed but not found any restaurants");
                        } else {
                            log.info("Reading restaurant by name completed");
                        }
                    });
        }

    @Override
    public Flux<RestaurantResponse> readByPriceRangeIn(List<PriceEnum> priceRanges) {
        log.info("Reading restaurants by price range: {}", priceRanges);

        final String cacheKey = CatalogCacheService.buildPriceKey(priceRanges);

        return this.restaurantCache.getCacheRestaurants(cacheKey)
                .switchIfEmpty(
                        this.restaurantCatalogService
                                .readByPriceRangeIn(priceRanges)
                                .transform(this.restaurantMapper::toResponseFlux)
                                .transform(restaurantsDB -> this.restaurantCache.cacheRestaurants(cacheKey,restaurantsDB))
                )
                .doOnComplete(() -> log.info("Reading restaurants by prices ranges completed"));
    }

    @Override
    public Flux<RestaurantResponse> readByCity(String city) {
        log.info("Reading restaurants by city: {}", city);

        final String cacheKey = CatalogCacheService.buildCityKey(city);

        return this.restaurantCache.getCacheRestaurants(cacheKey)
                    .switchIfEmpty(
                            this.restaurantCatalogService
                                    .readByCity(city)
                                    .transform(this.restaurantMapper::toResponseFlux)
                                    .transform(restaurantsDB -> this.restaurantCache.cacheRestaurants(cacheKey,restaurantsDB))
                    )
                    .doOnComplete(() -> log.info("Reading restaurants by city completed"));
    }
}
