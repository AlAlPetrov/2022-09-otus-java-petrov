package ru.otus.service;

import ru.otus.domain.Account;
import ru.otus.domain.AccountBalance;
import ru.otus.domain.Tariff;
import ru.otus.domain.TariffType;

import java.util.Optional;

public interface DataStore {

    TariffType saveTariffType(TariffType tariffType);

    Optional<TariffType> loadTariffType(Long id);

    Tariff saveTariff(Tariff tariffType);

    Optional<Tariff> loadTariff(Long id);

    Iterable<Tariff> loadTariffs();

    Account saveAccount(Account account);

    Optional<Account> loadAccount(Long id);
}
