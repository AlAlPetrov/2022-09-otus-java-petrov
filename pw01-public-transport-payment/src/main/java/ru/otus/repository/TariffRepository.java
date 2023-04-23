package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Tariff;

public interface TariffRepository extends CrudRepository<Tariff, Long> {
}
