package ru.otus.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

import java.util.HashSet;

@Getter
@Table("accountbalances")
public class AccountBalance {
    @Id
    private final Long id;

    @NonNull
    Long tariff;

    @NonNull
    Long remainingValue;

    @NonNull
    Long reservedValue;

    @PersistenceCreator
    protected AccountBalance(Long id,
                             @NonNull Long tariff,
                             @NonNull Long remainingValue,
                             @NonNull Long reservedValue) {
        this.id = id;
        this.tariff = tariff;
        this.remainingValue = remainingValue;
        this.reservedValue = reservedValue;
    }

    public AccountBalance(@NonNull Long tariff,
                          @NonNull Long remainingValue,
                          @NonNull Long reservedValue) {
        this(null, tariff, remainingValue, reservedValue);
    }

    public static AccountBalance fromRequest(AccountBalanceRequest accountBalanceRequest) {
        return new AccountBalance(accountBalanceRequest.tariff(),
                accountBalanceRequest.remainingValue(),
                accountBalanceRequest.reservedValue());
    }

    public static AccountBalance fromRequest(Long accountId,
                                             AccountBalanceRequest accountBalanceRequest) {
        return new AccountBalance(accountId,
                accountBalanceRequest.tariff(),
                accountBalanceRequest.remainingValue(),
                accountBalanceRequest.reservedValue());
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "id=" + id +
                '}';
    }
}
