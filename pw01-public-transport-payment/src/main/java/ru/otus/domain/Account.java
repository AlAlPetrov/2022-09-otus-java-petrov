package ru.otus.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
@Table("accounts")
public class Account {
    @Id
    private final Long id;
    @NonNull
    private final String cardNumber;
    //@Column("account")
    @MappedCollection(idColumn = "account")
    private final Set<AccountBalance> accountBalances;

    @PersistenceCreator
    protected Account(Long id,
                      @NonNull String cardNumber,
                      @NonNull Set<AccountBalance> accountBalances) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.accountBalances = new HashSet<>();
        for (var accountBalance: accountBalances) {
            this.accountBalances.add(accountBalance);
        };
    }

    public Account(@NonNull String cardNumber,
                   @NonNull Set<AccountBalance> accountBalance) {
        this(null, cardNumber, accountBalance);
    }

    public void addBalance(AccountBalance accountBalance) {
        accountBalances.add(accountBalance);
    }
    public static Account fromRequest(AccountRequest accountRequest) {
        var balances = new HashSet<AccountBalance>();
        for (var accountBalance: accountRequest.balances()) {
            balances.add(AccountBalance.fromRequest(accountBalance));
        }
        return new Account(accountRequest.cardNumber(),
                balances);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", cardNumber='" + cardNumber +
                '}';
    }
}
