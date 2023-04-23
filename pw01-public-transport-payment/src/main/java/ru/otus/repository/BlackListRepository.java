package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.BlackList;

import java.util.Optional;

public interface BlackListRepository extends CrudRepository<BlackList, Long> {
    @Query("select * from blacklist where account = :accountId")
    Optional<BlackList> findByAccountId(@Param("accountId") Long accountId);

    @Modifying
    @Query("delete from blacklist where account = :accountId")
    void deleteByAccountId(@Param("accountId") Long accountId);
}
