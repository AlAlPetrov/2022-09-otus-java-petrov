package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
