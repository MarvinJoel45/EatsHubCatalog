package com.debbugeandoideas.eats_hub_catalog.clientes;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Simula un microservicio que nos avisa sobre la disponibilidad de un restaurante, con su respectiva latencia.
 * PlannerMSCliente
 */
@Component 
@Slf4j 
public class PlannerMSCliente {

    private static final String UNAVAILABLE_RESTAURANT_ID = "dfcbe98d-392b-4b93-9a49-27005223d15d"; //ALWAYS is FULL!

    public Mono<Boolean> verifyAvailability(String date, String time, UUID restaurantID) {

        return Mono.fromCallable(() -> !UNAVAILABLE_RESTAURANT_ID.equals(restaurantID.toString())).delayElement(getRandomDuration())
        .doOnNext(reservation -> log.info("Checking availability for restaurant {}, date {}, time {}", restaurantID, time));
    }
    
    private Duration getRandomDuration() {
        final var randomint = ThreadLocalRandom.current().nextInt(20,1000);
        return Duration.ofMillis(randomint);
    }
    
}
