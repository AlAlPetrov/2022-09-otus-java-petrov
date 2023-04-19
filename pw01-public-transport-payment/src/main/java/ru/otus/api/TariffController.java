package ru.otus.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffDto;
import ru.otus.service.DataStore;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
public class TariffController {
    private static final Logger log = LoggerFactory.getLogger(TariffController.class);
    private final DataStore dataStore;
    private final Scheduler workerPool;

    public TariffController(DataStore dataStore, Scheduler workerPool) {
        this.dataStore = dataStore;
        this.workerPool = workerPool;
    }

    @PostMapping(value = "/tariff")
    public Mono<Long> createTariff(@RequestBody TariffDto tariffDto) {
        var savedId = Mono.just(new Tariff(tariffDto.type(),
                        tariffDto.name(),
                        tariffDto.price(),
                        tariffDto.initialValue()))
                .doOnNext(tariff -> log.debug("tariff:{}", tariff))
                .flatMap(dataStore::saveTariff)
                .publishOn(workerPool)
                .doOnNext(savedTariff -> log.debug("saved tariffType id:{}",
                        savedTariff.getId()))
                .map(Tariff::getId)
                .subscribeOn(workerPool);

        return savedId;
    }

    @GetMapping(value = "/tariff/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<TariffDto> getTariffById(@PathVariable("id") Integer id) {
        return dataStore.loadTariff(id)
                .map(tariff -> new TariffDto(tariff.getType(),
                        tariff.getName(),
                        tariff.getPrice(),
                        tariff.getInitialValue()))
                .subscribeOn(workerPool);
    }

    @GetMapping(value = "/tariff", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TariffDto> getTariffById() {
        return dataStore.loadTariffs()
                .map(tariff -> new TariffDto(tariff.getType(),
                        tariff.getName(),
                        tariff.getPrice(),
                        tariff.getInitialValue()))
                .subscribeOn(workerPool);
    }
}
