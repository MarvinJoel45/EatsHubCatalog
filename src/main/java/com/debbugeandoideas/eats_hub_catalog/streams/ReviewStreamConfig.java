package com.debbugeandoideas.eats_hub_catalog.streams;

import com.debbugeandoideas.eats_hub_catalog.dtos.events.ReviewEvent;
import com.debbugeandoideas.eats_hub_catalog.services.impl.ReviewStreamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ReviewStreamConfig {

    private final ObjectMapper objectMapper;
    private final ReviewStreamService reviewStreamService;

    @Bean
    public Consumer<Flux<Message<String>>> reviewEventConsumer() {
        return flux -> flux
                .doOnNext(msg -> log.info("Review event received: {}",msg.getPayload()))
                .flatMap(this::processMessage)
                .doOnError(err -> log.error("Error processing review event",err))
                .retry(3)
                .subscribe();
    }

    public Mono<Object> processMessage(Message<String> message) {
        return Mono.fromCallable(() -> {
            String payload = message.getPayload();
            return objectMapper.readValue(payload, ReviewEvent.class);
        })
        .flatMap(this.reviewStreamService::processReview)
        .onErrorResume(throwable -> {
            log.error(throwable.getMessage(),throwable);
            return Mono.empty();
        });
    }
}
