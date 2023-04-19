package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffType;

public interface DataStore {

    Mono<TariffType> saveTariffType(TariffType tariffType);

    Mono<TariffType> loadTariffType(Long id);

    Mono<Tariff> saveTariff(Tariff tariffType);

    Mono<Tariff> loadTariff(Integer id);

    Flux<Tariff> loadTariffs();
}
