package ru.otus.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Account;
import ru.otus.domain.AccountBalance;
import ru.otus.domain.BlackList;
import ru.otus.domain.PaymentRequest;
import ru.otus.exception.BadRequestException;
import ru.otus.service.DataStore;
import ru.otus.service.PaymentJdbc;

import java.util.Optional;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Payment service tests")
class PaymentServiceTest {
    static class SavedAccountBalance extends AccountBalance {
        protected SavedAccountBalance(Long id, AccountBalance accountBalance) {
            super(id,
                    accountBalance.getTariff(),
                    accountBalance.getRemainingValue(),
                    accountBalance.getReservedValue());
        }
    }

    static class SavedAccount extends Account {
        protected SavedAccount(Long id, Account account) {
            super(id,
                    account.getCardNumber(),
                    account.getAccountBalances());
        }
    }

    static private Account makeValidAccount() {
        return new SavedAccount(1L,
                new Account(randomUUID().toString(),
                        Set.of(new SavedAccountBalance(1L, new AccountBalance(1L,
                                        0L,
                                        20045L)),
                                new SavedAccountBalance(2L, new AccountBalance(2L,
                                        0L,
                                        40045L)))));
    }

    @DisplayName("WriteOff with happy path reduces balance value")
    @Test
    void writeOffAccountWithHappyPathReducesBalanceValue() {
        //arrange
        var value = 10032L;
        var dataStore = mock(DataStore.class);
        var paymentJdbc = new PaymentJdbc(dataStore);
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var accountBalance = accountFromDb.getAccountBalances().stream()
                .findFirst()
                .get();
        accountBalance.topUp(value);
        var balanceBeforeTopUp = (AccountBalance)
                accountBalance
                .clone();
        var request = new PaymentRequest(accountFromDb.getId(),
                balanceBeforeTopUp.getTariff(),
                value);

        //act
        paymentJdbc.writeOff(request);

        //assert
        verify(dataStore).saveAccount(argThat(savedAccount -> savedAccount.getAccountBalances().stream()
                .filter(balance -> balance.getTariff().equals(balanceBeforeTopUp.getTariff()))
                .findFirst()
                .get().getRemainingValue() == (balanceBeforeTopUp.getRemainingValue() - value)));
    }

    @DisplayName("WriteOff account with non-existent account throws BadRequest exception")
    @Test
    void writeOffAccountWithNonExistentAccountThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var paymentJdbc = new PaymentJdbc(dataStore);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var request = new PaymentRequest(1L,
                2L,
                3L);

        //assert
        assertThatThrownBy(() -> paymentJdbc.writeOff(request))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("WriteOff account with non-existent balance throws BadRequest exception")
    @Test
    void writeOffAccountWithNonExistentBalanceThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var paymentJdbc = new PaymentJdbc(dataStore);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var request = new PaymentRequest(1L,
                1002L,
                3L);

        //assert
        assertThatThrownBy(() -> paymentJdbc.writeOff(request))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("WriteOff account with account in black list throws BadRequest exception")
    @Test
    void writeOffAccountWithAccountInBlackListThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var paymentJdbc = new PaymentJdbc(dataStore);
        var blackListItem = new BlackList(1L, "test reason");
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        given(dataStore.loadBlackList(anyLong())).willReturn(Optional.of(blackListItem));
        var request = new PaymentRequest(1L,
                1002L,
                3L);

        //assert
        assertThatThrownBy(() -> paymentJdbc.writeOff(request))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("WriteOff with several balances throws BadRequest exception")
    @Test
    void writeOffAccountWithSeveralBalancesThrows() {
        //arrange
        var value = 10032L;
        var dataStore = mock(DataStore.class);
        var paymentJdbc = new PaymentJdbc(dataStore);
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var balanceBeforeTopUp = (AccountBalance)
                accountFromDb.getAccountBalances().stream()
                        .findFirst()
                        .get()
                        .clone();
        accountFromDb.addBalance(balanceBeforeTopUp);
        var request = new PaymentRequest(accountFromDb.getId(),
                balanceBeforeTopUp.getTariff(),
                value);

        //assert
        assertThatThrownBy(() -> paymentJdbc.writeOff(request))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("WriteOff with not enough funds throws BadRequest exception")
    @Test
    void writeOffAccountWithNotEnoughFundsThrows() {
        //arrange
        var value = 10032L;
        var dataStore = mock(DataStore.class);
        var paymentJdbc = new PaymentJdbc(dataStore);
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var accountBalance = accountFromDb.getAccountBalances().stream()
                .findFirst()
                .get();
        accountBalance.topUp(value - 1);
        var balanceBeforeTopUp = (AccountBalance)
                accountBalance
                        .clone();
        var request = new PaymentRequest(accountFromDb.getId(),
                balanceBeforeTopUp.getTariff(),
                value);

        //assert
        assertThatThrownBy(() -> paymentJdbc.writeOff(request))
                .isInstanceOf(BadRequestException.class);
    }
}