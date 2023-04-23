package ru.otus.service;

import ru.otus.domain.*;

import java.util.Optional;

public interface DataStore {

    TariffType saveTariffType(TariffType tariffType);

    Optional<TariffType> loadTariffType(Long id);

    Tariff saveTariff(Tariff tariffType);

    Optional<Tariff> loadTariff(Long id);

    Iterable<Tariff> loadTariffs();

    Account saveAccount(Account account);

    Optional<Account> loadAccount(Long id);

    Optional<BlackList> loadBlackList(Long accountId);

    BlackList saveBlackList(BlackList blackList);

    void deleteBlackList(Long accountId);
}
