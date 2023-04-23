package ru.otus.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Getter
@Table("accountbalances")
public class AccountBalance implements Cloneable {
    @Id
    private final Long id;

    @NonNull
    Long tariff;

    @NonNull
    Long remainingValue;

    @NonNull
    Long reservedValue;

    public static AccountBalance fromRequest(AccountBalanceRequest accountBalanceRequest) {
        return new AccountBalance(accountBalanceRequest.tariff(),
                accountBalanceRequest.remainingValue(),
                accountBalanceRequest.reservedValue());
    }

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

    public void topUp(Long value) {
        remainingValue += value;
    }

    public void writeOff(Long value) {
        if (remainingValue < value) {
            throw new RuntimeException("Not enough funds");
        }
        remainingValue -= value;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "id=" + id +
                '}';
    }

    public Object clone() {
        return new AccountBalance(this.id,
                this.tariff,
                this.remainingValue,
                this.reservedValue);
    }

}
