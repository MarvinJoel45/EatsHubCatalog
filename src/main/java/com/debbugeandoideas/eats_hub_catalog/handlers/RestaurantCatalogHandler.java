package com.debbugeandoideas.eats_hub_catalog.handlers;

import com.debbugeandoideas.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.RestaurantBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RestaurantCatalogHandler {

    private final RestaurantBusinessService restaurantBusinessService;

    /**
     * El serverResponse es un objeto usado para construir la respuesta HTTP que se enviará al cliente.
     * Similar al ResponseEntity en Spring MVC.
     * Con la diferencia que ServerResponse es parte del modelo de programación reactiva de Spring WebFlux.
     * Lo mismo con el ServerRequest, que representa la solicitud HTTP entrante.
     * @return
     */
    public Mono<ServerResponse> getAllRestaurants(ServerRequest serverRequest) {

        final Integer page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        final Integer size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));

        final var restaurantFlux = this.restaurantBusinessService.readAll(page,size);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(restaurantFlux, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantByName(ServerRequest serverRequest) {
        final var restaurantName = serverRequest.pathVariable("name");
        final var monoResponse = this.restaurantBusinessService.readByName(restaurantName);

        return monoResponse
                .flatMap(restaurantResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(restaurantResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantByCousinType(ServerRequest serverRequest) {
        final var cousineType = serverRequest.queryParam("cousinType").orElse(null);

        if (Objects.isNull(cousineType)) {
            return ServerResponse.badRequest().bodyValue("Missing required query parameter: cousinType");
        }

        final var fluxResponse = this.restaurantBusinessService.readByCuisineType(cousineType);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantBetweenPrice(ServerRequest serverRequest) {
        final var prices = serverRequest.queryParam("prices").orElse(null);

        if (Objects.isNull(prices)) {
            return ServerResponse.badRequest().bodyValue("Missing required query parameter: prices");
        }

        final var typesList = Arrays.stream(prices.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(PriceEnum::valueOf)
                .toList();

        final var fluxResponse = this.restaurantBusinessService.readByPriceRangeIn(typesList);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getRestaurantsByCity(ServerRequest serverRequest) {
        final var city = serverRequest.queryParam("city").orElse(null);

        if (Objects.isNull(city)) {
            return ServerResponse.badRequest().bodyValue("Missing required query parameter: city");
        }

        final var fluxRestaurants = this.restaurantBusinessService.readByCity(city);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxRestaurants, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
