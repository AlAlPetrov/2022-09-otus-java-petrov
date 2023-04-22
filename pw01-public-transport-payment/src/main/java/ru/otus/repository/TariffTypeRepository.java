package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.TariffType;

public interface TariffTypeRepository extends CrudRepository<TariffType, Long> {
}
