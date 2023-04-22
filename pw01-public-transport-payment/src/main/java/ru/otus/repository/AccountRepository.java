package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    //@Query("select * from accounts a left join accountBalances ab on a.id = ab.account where a.id = :id")
    //Optional<Account> findById(@Param("id") Long id);
}
