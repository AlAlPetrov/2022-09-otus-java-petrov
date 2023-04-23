package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.domain.*;
import ru.otus.exception.BadRequestException;

import java.util.stream.Collectors;

@Service
public class TopUpJdbc implements TopUp {
    private static final Logger log = LoggerFactory.getLogger(TopUpJdbc.class);
    private final DataStore dataStore;

    public TopUpJdbc(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Account topUpAccount(PaymentRequest paymentRequest) {
        var account = dataStore
                .loadAccount(paymentRequest.accountId())
                .orElseThrow(() -> new BadRequestException("Account with id " +
                paymentRequest.accountId() +
                " not found"));

        var isInBlackList = dataStore
                .loadBlackList(paymentRequest.accountId())
                .orElse(null) != null;
        if (isInBlackList) {
            throw new BadRequestException("Account with id " +
                    paymentRequest.accountId() +
                    " is in the black list");
        }

        var balanceToTopUp = account.getAccountBalances()
                .stream()
                .filter(balance -> balance.getTariff() == paymentRequest.tariffId())
                .collect(Collectors.toList());
        if (balanceToTopUp.size() == 0) {
            throw new BadRequestException("No balance for the tariff " +
                    paymentRequest.tariffId() +
                    " found");
        }
        if (balanceToTopUp.size() > 1) {
            throw new BadRequestException("Too many balances for the tariff " +
                    paymentRequest.tariffId() +
                    " found");
        }
        balanceToTopUp.get(0).topUp(paymentRequest.value());

        return dataStore.saveAccount(account);
    }
}
