package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.domain.*;
import ru.otus.repository.AccountRepository;
import ru.otus.repository.TariffRepository;
import ru.otus.repository.TariffTypeRepository;

import java.util.Optional;

@Service
public class DataStoreJdbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreJdbc.class);
    private final TariffTypeRepository tariffTypeRepository;
    private final TariffRepository tariffRepository;
    private final AccountRepository accountRepository;

    public DataStoreJdbc(TariffTypeRepository tariffTypeRepository,
                         TariffRepository tariffRepository,
                         AccountRepository accountRepository) {
        this.tariffTypeRepository = tariffTypeRepository;
        this.tariffRepository = tariffRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TariffType saveTariffType(TariffType tariffType) {
        log.debug("saveTariffType:{}", tariffType);
        return tariffTypeRepository.save(tariffType);
    }

    @Override
    public Optional<TariffType> loadTariffType(Long id) {
        log.debug("loadTariffType id:{}", id);
        return tariffTypeRepository.findById(id);
    }

    @Override
    public Tariff saveTariff(Tariff tariffType) {
        log.debug("saveTariffType:{}", tariffType);
        return tariffRepository.save(tariffType);
    }

    @Override
    public Optional<Tariff> loadTariff(Long id) {
        log.debug("loadTariff id:{}", id);
        return tariffRepository.findById(id);
    }

    @Override
    public Iterable<Tariff> loadTariffs() {
        log.debug("loadTariffs");
        return tariffRepository.findAll();
    }

    @Override
    public Account saveAccount(Account account) {
        log.debug("saveAccount");
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> loadAccount(Long id) {
        log.debug("loadAccount id:{}", id);
        return accountRepository.findById(id);
    }
}
