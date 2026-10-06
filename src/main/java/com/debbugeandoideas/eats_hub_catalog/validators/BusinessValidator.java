package com.debbugeandoideas.eats_hub_catalog.validators;

import reactor.core.publisher.Mono;

/**
 * Este interface si no pasa una validacion va a retornar un exception
 * BusinessValidator
 */
@FunctionalInterface // Para poder implementarla como expresion lambda
public interface BusinessValidator<T> {


    Mono<Void> validate(T input);
}
