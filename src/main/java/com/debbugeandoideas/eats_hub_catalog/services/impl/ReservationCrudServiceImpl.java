package com.debbugeandoideas.eats_hub_catalog.services.impl;

import com.debbugeandoideas.eats_hub_catalog.collections.ReservationCollection;
import com.debbugeandoideas.eats_hub_catalog.enums.ReservationStatusEnum;
import com.debbugeandoideas.eats_hub_catalog.exceptions.ResourceNotFoundException;
import com.debbugeandoideas.eats_hub_catalog.repositories.ReservationRepository;
import com.debbugeandoideas.eats_hub_catalog.repositories.RestaurantRepository;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReservationCrudService;
import com.debbugeandoideas.eats_hub_catalog.validators.ReservationValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationCrudServiceImpl implements ReservationCrudService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationValidator reservationValidator;

    @Override
    public Mono<ReservationCollection> createReservation(ReservationCollection reservation) {

        reservation.setId(UUID.randomUUID());

        final var validations = List.of(
            this.reservationValidator.validateRestaurantNotClosed(),
            this.reservationValidator.validateAvailability()
        );

        return this.reservationValidator.applyValidations(reservation, validations)
            .then(
                this.restaurantRepository.findById(UUID.fromString(reservation.getRestaurantId()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
            )
            .flatMap(restaurant -> {
                if (Objects.isNull(reservation.getStatus())) {
                    reservation.setStatus(ReservationStatusEnum.PENDING);
                }

                log.info("Creating reservation with id {}", reservation.getId());

                return this.reservationRepository.save(reservation);
            });
    }

    @Override
    public Mono<ReservationCollection> readByReservationId(UUID id) {
        return this.reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")));
    }

    @Override
    public Flux<ReservationCollection> readByRestaurantId(UUID restaurantId, ReservationStatusEnum status) {
        return this.restaurantRepository.findById(restaurantId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                .flatMapMany(restaurant -> {
                    if (Objects.isNull(status)) {
                        return this.reservationRepository.findByRestaurantId(restaurantId.toString());
                    }

                    log.info("Reading reservation with id {} and status {}", restaurantId, status);

                    return this.reservationRepository.findByRestaurantIdAndStatus(restaurantId.toString(),status);
                });
    }

    @Override
    public Mono<ReservationCollection> updateReservation(UUID id, ReservationCollection reservation) {

        final var validations = List.of(
            this.reservationValidator.validateRestaurantNotClosed()
        );

        return this.reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")))
                .flatMap(existingReservation -> {
                    reservation.setRestaurantId(existingReservation.getRestaurantId());

                    return this.reservationValidator.applyValidations(reservation, validations)
                            .thenReturn(existingReservation);
                })
                .flatMap(existingReservation -> {

                    log.info("Updating reservation with id {}", existingReservation.getId());

                    existingReservation.setStatus(reservation.getStatus());
                    existingReservation.setNotes(reservation.getNotes());
                    existingReservation.setDate(reservation.getDate());
                    existingReservation.setTime(reservation.getTime());
                    existingReservation.setCustomerName(reservation.getCustomerName());
                    existingReservation.setPartySize(reservation.getPartySize());

                    return this.reservationRepository.save(existingReservation);
                });
    }

    @Override
    public Mono<Void> deleteReservation(UUID id) {
        return this.reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")))
                .flatMap(reservation -> {
                    log.info("Deleting reservation with id {}", reservation.getId());

                    return this.reservationRepository.deleteById(reservation.getId());
                });
    }
}
