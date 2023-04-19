package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.domain.Tariff;

public interface TariffRepository extends ReactiveCrudRepository<Tariff, Long> {

    @Query("select * from tariffs where id = :id")
    Mono<Tariff> findById(@Param("id") Integer id);
}
