package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.AccountBalance;

public interface AccountBalanceRepository extends CrudRepository<AccountBalance, Long> {
    @Query("delete accountBalances account = :accountId and id = :id ")
    void deleteAccountBalanceById(@Param("accountId") Long accountId,
                                  @Param("id") Long id);
}
