package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.domain.Account;
import ru.otus.domain.PaymentRequest;
import ru.otus.exception.BadRequestException;

import java.util.stream.Collectors;

@Service
public class PaymentJdbc implements Payment {
    private static final Logger log = LoggerFactory.getLogger(PaymentJdbc.class);
    private final DataStore dataStore;

    public PaymentJdbc(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Account writeOff(PaymentRequest paymentRequest) {
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

        var balanceToWriteOff = account.getAccountBalances()
                .stream()
                .filter(balance -> balance.getTariff() == paymentRequest.tariffId())
                .collect(Collectors.toList());
        if (balanceToWriteOff.size() == 0) {
            throw new BadRequestException("No balance for the tariff " +
                    paymentRequest.tariffId() +
                    " found");
        }
        if (balanceToWriteOff.size() > 1) {
            throw new BadRequestException("Too many balances for the tariff " +
                    paymentRequest.tariffId() +
                    " found");
        }

        if (balanceToWriteOff.get(0).getRemainingValue() < paymentRequest.value()) {
            throw new BadRequestException("Not enough funds on balance " +
                    paymentRequest.tariffId() +
                    " to complete the payment ");
        }

        balanceToWriteOff.get(0).writeOff(paymentRequest.value());

        return dataStore.saveAccount(account);
    }
}
