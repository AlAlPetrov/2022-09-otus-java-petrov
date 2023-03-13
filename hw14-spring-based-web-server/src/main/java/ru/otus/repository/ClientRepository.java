package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Client;


public interface ClientRepository extends CrudRepository<Client, Long> {
}
