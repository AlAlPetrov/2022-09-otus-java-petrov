package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.domain.TariffType;

public interface TariffTypeRepository extends ReactiveCrudRepository<TariffType, Long> {

    @Query("select * from tariffTypes where id = :id")
    Mono<TariffType> findById(@Param("id") Long id);
}
