package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffType;
import ru.otus.repository.TariffRepository;
import ru.otus.repository.TariffTypeRepository;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private final TariffTypeRepository tariffTypeRepository;
    private final TariffRepository tariffRepository;
    private final Scheduler workerPool;

    public DataStoreR2dbc(Scheduler workerPool,
                          TariffTypeRepository tariffTypeRepository,
                          TariffRepository tariffRepository) {
        this.workerPool = workerPool;
        this.tariffTypeRepository = tariffTypeRepository;
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Mono<TariffType> saveTariffType(TariffType tariffType) {
        log.debug("saveTariffType:{}", tariffType);
        return tariffTypeRepository.save(tariffType);
    }

    @Override
    public Mono<TariffType> loadTariffType(Long id) {
        log.debug("loadTariffType id:{}", id);
        return tariffTypeRepository.findById(id);
    }

    @Override
    public Mono<Tariff> saveTariff(Tariff tariffType) {
        log.debug("saveTariffType:{}", tariffType);
        return tariffRepository.save(tariffType);
    }

    @Override
    public Mono<Tariff> loadTariff(Integer id) {
        log.debug("loadTariff id:{}", id);
        return tariffRepository.findById(id);
    }

    @Override
    public Flux<Tariff> loadTariffs() {
        log.debug("loadTariffs");
        return tariffRepository.findAll();
    }
}
