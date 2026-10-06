package com.debbugeandoideas.eats_hub_catalog.services.impl;

import com.debbugeandoideas.eats_hub_catalog.dtos.requests.ReservationRequest;
import com.debbugeandoideas.eats_hub_catalog.dtos.responses.ReservationResponse;
import com.debbugeandoideas.eats_hub_catalog.enums.ReservationStatusEnum;
import com.debbugeandoideas.eats_hub_catalog.mappers.ReservationMapper;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReservationBusinessService;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReservationCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationBusinessServiceImpl implements ReservationBusinessService {

    private final ReservationCrudService reservationCrudService;
    private final ReservationMapper reservationMapper;

    @Override
    public Mono<String> createReservation(ReservationRequest request) {
        log.info("Creating reservation {}", request);

        return Mono.just(request)
                .transform(this.reservationMapper::toCollectionMono)
                .flatMap(this.reservationCrudService::createReservation)
                .map(savedReservation -> {
                    log.info("Saving reservation: {}", savedReservation);
                    return savedReservation.getId().toString();
                });
    }

    @Override
    public Mono<ReservationResponse> readByReservationId(UUID id) {
        log.info("Reading reservation with id {}", id);

        return this.reservationCrudService.readByReservationId(id)
                .transform(this.reservationMapper::toResponseMono)
                .doOnSuccess(reservation -> log.info("Read reservation with id {} successfully", id));
    }

    @Override
    public Flux<ReservationResponse> readByRestaurantId(UUID restaurantId, ReservationStatusEnum status) {
        log.info("Reading reservation with restaurant id {}", restaurantId);

        return this.reservationCrudService.readByRestaurantId(restaurantId, status)
                .transform(this.reservationMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Read reservations with restaurant id {} successfully", restaurantId));
    }

    @Override
    public Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation) {
        log.info("Updating reservation with id {}", id);

        return Mono.just(reservation)
                .transform(this.reservationMapper::toCollectionMono)
                .flatMap(reservationCollection -> this.reservationCrudService.updateReservation(id, reservationCollection))
                .transform(this.reservationMapper::toResponseMono)
                .doOnNext(reservationResponse -> log.info("Updating reservation with id {} successfully", id));
    }

    @Override
    public Mono<Void> deleteReservation(UUID id) {
        log.info("Deleting reservation with id {}", id);

        return this.reservationCrudService.deleteReservation(id)
                .doOnSuccess(VOID -> log.info("Deleting reservation with id {} successfully", id));
    }
}
