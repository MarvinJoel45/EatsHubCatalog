package com.debbugeandoideas.eats_hub_catalog.handlers;

import com.debbugeandoideas.eats_hub_catalog.dtos.requests.ReservationRequest;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReservationBusinessService;
import com.debbugeandoideas.eats_hub_catalog.validators.ReactiveValidator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReservationHandler {

    private final ReservationBusinessService reservationBusinessService;
    private final ReactiveValidator validator;

    public Mono<ServerResponse> postReservation(ServerRequest request) {
        return request.bodyToMono(ReservationRequest.class)
                .flatMap(this.validator::validate)
                .flatMap(this.reservationBusinessService::createReservation)
                .flatMap(id ->
                    ServerResponse
                            .created(URI.create("/reservation/" + id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("Resource", "/reservation/" + id)))
                .doOnSuccess(response -> log.info("Reservation created: {}",response))
                .doOnError(error -> log.error("Error while creating reservation with error: {}", error.getMessage()));
    }

    public Mono<ServerResponse> getReservationById(ServerRequest request) {
        final var id = request.pathVariable("id");

        return this.parseUUID(id)
                .flatMap(this.reservationBusinessService::readByReservationId)
                .flatMap(reservationResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservationResponse))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSuccess(response -> log.info("Reservation found: {}", response))
                .doOnError(error -> log.error("Error while reading reservation with error: {}", error.getMessage()));
    }

    public Mono<ServerResponse> updateReservation(ServerRequest request) {
        final var id = request.pathVariable("id");

        return this.parseUUID(id)
                .flatMap(uuid -> request.bodyToMono(ReservationRequest.class)
                        .flatMap(this.validator::validate)
                        .flatMap(reservationReq -> this.reservationBusinessService.updateReservation(uuid, reservationReq)))
                .flatMap(updateReservation -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updateReservation))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSuccess(response -> log.info("Reservation updated: {}", response))
                .doOnError(error -> log.error("Error while updating reservation with error: {}", error.getMessage()));
    }

    public Mono<ServerResponse> deleteReservation(ServerRequest request) {
        final var id = request.pathVariable("id");

        return this.parseUUID(id)
                .flatMap(this.reservationBusinessService::deleteReservation)
                .then(ServerResponse.noContent().build())
                .doOnSuccess(response -> log.info("Reservation deleted: {}", response))
                .doOnError(error -> log.error("Error while deleting reservation with error: {}", error.getMessage()));
    }

    private Mono<UUID> parseUUID (String uuid) {

        try {
            return Mono.just(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ValidationException("Invalid UUID: " + uuid));
        }
    }
}
