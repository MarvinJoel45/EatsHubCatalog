package com.debbugeandoideas.eats_hub_catalog.routes;

import com.debbugeandoideas.eats_hub_catalog.handlers.ReservationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReservationCrudRouter {

    @Bean
    public RouterFunction<ServerResponse> reservationRoutes(ReservationHandler handler) {

        return route()
                .path("/reservation", builder -> builder
                        .POST("", handler::postReservation)
                        .GET("/{id}", handler::getReservationById)
                        .PUT("/{id}", handler::updateReservation)
                        .DELETE("/{id}", handler::deleteReservation))
                .build();
    }
}
