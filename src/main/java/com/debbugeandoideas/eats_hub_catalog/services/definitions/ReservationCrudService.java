package com.debbugeandoideas.eats_hub_catalog.services.definitions;

import com.debbugeandoideas.eats_hub_catalog.collections.ReservationCollection;
import com.debbugeandoideas.eats_hub_catalog.enums.ReservationStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationCrudService {

    Mono<ReservationCollection> createReservation(ReservationCollection reservation);

    Mono<ReservationCollection> readByReservationId(UUID id);

    Flux<ReservationCollection> readByRestaurantId(UUID restaurantId, ReservationStatusEnum status);

    Mono<ReservationCollection> updateReservation(UUID id, ReservationCollection reservation);

    Mono<Void> deleteReservation(UUID id);
}
