package ru.otus.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.api.AccountController;
import ru.otus.api.ResourceNotFoundException;
import ru.otus.domain.*;
import ru.otus.service.DataStore;

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

@DisplayName("Account controller tests")
class AccountTest {
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

    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
    }

    @DisplayName("Get account with existing id returns account")
    @Test
    void getAccountWithExistingIdReturnsAccount() {
        //arrange
        var dataStore = mock(DataStore.class);
        var accountController = new AccountController(dataStore);
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));

        //act
        var account = accountController.getAccountById(anyLong());

        //assert
        assertThat(account).isEqualTo(accountFromDb);
    }

    @DisplayName("Get account with non-existent id throws ResourceNotFound exception")
    @Test
    void getAccountWithNonExistentIdThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var accountController = new AccountController(dataStore);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());

        //assert
        assertThatThrownBy(() -> accountController.getAccountById(anyLong()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("Save account saves account to db and returns saved account")
    @Test
    void saveAccountSavesAccountToDbAndReturnsSavedAccount() {
        //arrange
        var savedAccount = makeValidAccount();
        var dataStore = mock(DataStore.class);
        var accountController = new AccountController(dataStore);
        var accountRequest = new AccountRequest(randomUUID().toString(),
                List.of(new AccountBalanceRequest(1L,
                        123L,
                        321L)));
        given(dataStore.saveAccount(any(Account.class))).willReturn(savedAccount);

        //act
        var account = accountController.createAccount(accountRequest);

        //assert
        assertThat(account).isEqualTo(savedAccount);
    }

    @DisplayName("Add account balance to existing account returns account with new balance")
    @Test
    void addBalanceToExistingAccountReturnsAccount() {
        //arrange
        var savedAccount = makeValidAccount();
        var dataStore = mock(DataStore.class);
        var accountController = new AccountController(dataStore);
        var accountBalanceRequest = new AccountBalanceRequest(1L,
                123L,
                321L);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(savedAccount));
        given(dataStore.saveAccount(any(Account.class))).willReturn(savedAccount);

        //act
        var account = accountController.addBalanceToAccount(anyLong(), accountBalanceRequest);

        //assert
        assertThat(account).isEqualTo(savedAccount);
    }

    @DisplayName("Add account balance to non-existent account throws ResourceNotFound exception")
    @Test
    void addBalanceToNonExistentAccountThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var accountController = new AccountController(dataStore);
        var accountBalanceRequest = new AccountBalanceRequest(1L,
                123L,
                321L);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());

        //assert
        assertThatThrownBy(() -> accountController.addBalanceToAccount(anyLong(),
                accountBalanceRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("Delete account balance to existing account deletes account balance")
    @Test
    void deleteBalanceOfExistingAccountDeletesBalance() {
        //arrange
        var dataStore = mock(DataStore.class);
        var accountController = new AccountController(dataStore);

        //act
        accountController.deleteAccountBalance(anyLong(), anyLong());

        //assert
        verify(dataStore, times(1))
                .deleteAccountBalanceById(anyLong(), anyLong());
    }
}