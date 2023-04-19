package ru.otus.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.TariffType;
import ru.otus.domain.TariffTypeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.service.DataStore;

@RestController
public class TariffTypeController {
    private static final Logger log = LoggerFactory.getLogger(TariffTypeController.class);
    private final DataStore dataStore;
    private final Scheduler workerPool;

    public TariffTypeController(DataStore dataStore, Scheduler workerPool) {
        this.dataStore = dataStore;
        this.workerPool = workerPool;
    }

    @PostMapping(value = "/tariffType")
    public Mono<Long> createTariffType(@RequestBody TariffTypeDto tariffTypeDto) {
        var tariffTypeName = tariffTypeDto.name();
        var savedId = Mono.just(new TariffType(tariffTypeName))
                .doOnNext(tariffType -> log.debug("tariffType:{}", tariffType))
                .flatMap(dataStore::saveTariffType)
                .publishOn(workerPool)
                .doOnNext(savedTariffType -> log.debug("saved tariffType id:{}",
                        savedTariffType.getId()))
                .map(TariffType::getId)
                .subscribeOn(workerPool);

        return savedId;
    }

    @GetMapping(value = "/tariffType/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<TariffTypeDto> getTariffTypeById(@PathVariable("id") Long id) {
        return  Mono.just(id)
                .flatMap(dataStore::loadTariffType)
                .map(tariffType -> new TariffTypeDto(tariffType.getName()))
                .subscribeOn(workerPool);
    }
}
