package com.debbugeandoideas.eats_hub_catalog.configs;

import com.debbugeandoideas.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

/**
 * Redis configuration estatica
 * 3 metodos tal cual la documentacion
 * Esta configuracion no se mueve porque solo es la forma de serializar y deserializar los objetos que se guardan en redis
 * En clave valor
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig {

    @Value("localhost")
    private String redisHost;

    @Value("6379")
    private int redisPort;

    @Value("debuggeandoideas")
    private String redisPassword;

    @Value("0")
    private int redisDatabase;

    /**
     * Metodo para crear la conexion a redis
     * @return
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        log.info("Connecting to Redis at: {},{}", redisHost,redisPort);

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        redisConfig.setPassword(redisPassword);
        redisConfig.setDatabase(0);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(3))
                .shutdownTimeout(Duration.ofMillis(150))
                .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    /**
     * Metodo para crear el template de redis para RestaurantResponse
     * @param connectionFactory
     * @return
     */
    @Bean
    public ReactiveRedisTemplate<String, RestaurantResponse> reactiveRedisTemplate(
            LettuceConnectionFactory connectionFactory) {

        Jackson2JsonRedisSerializer<RestaurantResponse> serializer =
                new Jackson2JsonRedisSerializer<>(createObjectMapper(), RestaurantResponse.class);

        RedisSerializationContext<String, RestaurantResponse> context =
                RedisSerializationContext.<String, RestaurantResponse>newSerializationContext()
                        .key(StringRedisSerializer.UTF_8)
                        .value(serializer)
                        .hashKey(StringRedisSerializer.UTF_8)
                        .hashValue(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    /**
     * Metodo para crear el template de redis para List<RestaurantResponse>
     * @param connectionFactory
     * @return
     */
    @Bean
    public ReactiveRedisTemplate<String, List<RestaurantResponse>> reactiveRedisListTemplate(
            LettuceConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = createObjectMapper();
        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, RestaurantResponse.class);

        Jackson2JsonRedisSerializer<List<RestaurantResponse>> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, type);

        RedisSerializationContext<String, List<RestaurantResponse>> context =
                RedisSerializationContext.<String, List<RestaurantResponse>>newSerializationContext()
                        .key(StringRedisSerializer.UTF_8)
                        .value(serializer)
                        .hashKey(StringRedisSerializer.UTF_8)
                        .hashValue(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    /**
     * Metodo para permitirnos ver la conexion cuando se conecte
     */
    @EventListener(ApplicationReadyEvent.class)
    public void verifyRedisConnection () {
        log.info("Connection successfully established.");
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}


