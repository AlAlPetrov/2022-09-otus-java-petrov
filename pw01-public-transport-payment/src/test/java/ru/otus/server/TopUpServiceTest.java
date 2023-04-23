package ru.otus.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.api.AccountController;
import ru.otus.api.TopUpController;
import ru.otus.domain.*;
import ru.otus.exception.BadRequestException;
import ru.otus.exception.ResourceNotFoundException;
import ru.otus.service.DataStore;
import ru.otus.service.TopUpJdbc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("TopUp controller tests")
class TopUpServiceTest {
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
                                        10012L,
                                        20045L)),
                                new SavedAccountBalance(2L, new AccountBalance(2L,
                                        30012L,
                                        40045L)))));
    }

    @DisplayName("TopUp with happy path adds value to the balance")
    @Test
    void topUpAccountWithHappyPathAddsValueToTheBalance() {
        //arrange
        var value = 10032L;
        var dataStore = mock(DataStore.class);
        var topUpService = new TopUpJdbc(dataStore);
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var balanceBeforeTopUp = (AccountBalance)
                accountFromDb.getAccountBalances().stream()
                .findFirst()
                .get()
                .clone();
        var topUpRequest = new TopUpRequest(accountFromDb.getId(),
                balanceBeforeTopUp.getTariff(),
                value);
        //act
        topUpService.topUpAccount(topUpRequest);

        //assert
        verify(dataStore).saveAccount(argThat(savedAccount -> savedAccount.getAccountBalances().stream()
                .filter(balance -> balance.getTariff() == balanceBeforeTopUp.getTariff())
                .findFirst()
                .get().getRemainingValue() == (balanceBeforeTopUp.getRemainingValue() + value)));
    }

    @DisplayName("TopUp account with non-existent account throws BadRequest exception")
    @Test
    void topUpAccountWithNonExistentAccountThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var topUpService = new TopUpJdbc(dataStore);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var topUpRequest = new TopUpRequest(1L,
                2L,
                3L);

        //assert
        assertThatThrownBy(() -> topUpService.topUpAccount(topUpRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("TopUp account with non-existent balance throws BadRequest exception")
    @Test
    void topUpAccountWithNonExistentBalanceThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var topUpService = new TopUpJdbc(dataStore);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var topUpRequest = new TopUpRequest(1L,
                1002L,
                3L);

        //assert
        assertThatThrownBy(() -> topUpService.topUpAccount(topUpRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("TopUp account with account in black list throws BadRequest exception")
    @Test
    void topUpAccountWithAccountInBlackListThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var topUpService = new TopUpJdbc(dataStore);
        var blackListItem = new BlackList(1L, "test reason");
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        given(dataStore.loadBlackList(anyLong())).willReturn(Optional.of(blackListItem));
        var topUpRequest = new TopUpRequest(1L,
                1002L,
                3L);

        //assert
        assertThatThrownBy(() -> topUpService.topUpAccount(topUpRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("TopUp with several balances throws BadRequest exception")
    @Test
    void topUpAccountWithSeveralBalancesThrows() {
        //arrange
        var value = 10032L;
        var dataStore = mock(DataStore.class);
        var topUpService = new TopUpJdbc(dataStore);
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        var balanceBeforeTopUp = (AccountBalance)
                accountFromDb.getAccountBalances().stream()
                        .findFirst()
                        .get()
                        .clone();
        accountFromDb.addBalance(balanceBeforeTopUp);
        var topUpRequest = new TopUpRequest(accountFromDb.getId(),
                balanceBeforeTopUp.getTariff(),
                value);

        //assert
        assertThatThrownBy(() -> topUpService.topUpAccount(topUpRequest))
                .isInstanceOf(BadRequestException.class);
    }
}