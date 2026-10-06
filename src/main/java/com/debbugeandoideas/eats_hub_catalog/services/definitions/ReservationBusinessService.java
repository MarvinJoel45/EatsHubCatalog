package com.debbugeandoideas.eats_hub_catalog.services.definitions;

import com.debbugeandoideas.eats_hub_catalog.collections.ReservationCollection;
import com.debbugeandoideas.eats_hub_catalog.dtos.requests.ReservationRequest;
import com.debbugeandoideas.eats_hub_catalog.dtos.responses.ReservationResponse;
import com.debbugeandoideas.eats_hub_catalog.enums.ReservationStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationBusinessService {
    Mono<String> createReservation(ReservationRequest request);

    Mono<ReservationResponse> readByReservationId(UUID id);

    Flux<ReservationResponse> readByRestaurantId(UUID restaurantId, ReservationStatusEnum status);

    Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation);

    Mono<Void> deleteReservation(UUID id);
}
