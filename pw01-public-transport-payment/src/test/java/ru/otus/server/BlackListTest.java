package ru.otus.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.api.AccountController;
import ru.otus.api.BlackListController;
import ru.otus.api.ResourceNotFoundException;
import ru.otus.domain.*;
import ru.otus.service.DataStore;

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
class BlackListTest {
    static class SavedBlackList extends BlackList {
        protected SavedBlackList(Long id, BlackList blackList) {
            super(id,
                    blackList.getAccountId(),
                    blackList.getReason());
        }
    }

    static private Account makeValidAccount() {
        return new AccountTest.SavedAccount(1L,
                new Account(randomUUID().toString(),
                        Set.of(new AccountTest.SavedAccountBalance(1L, new AccountBalance(1L,
                                        10012L,
                                        20045L)),
                                new AccountTest.SavedAccountBalance(2L, new AccountBalance(2L,
                                        30012L,
                                        40045L)))));
    }

    @DisplayName("Get blacklist with account in blackList returns blackList item")
    @Test
    void getBlackListWithAccountInBlacklistReturnsBlackListItem() {
        //arrange
        var dataStore = mock(DataStore.class);
        var blackListController = new BlackListController(dataStore);
        var savedBlackList = new SavedBlackList(1L,
                new BlackList(2L, "test reason"));
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        given(dataStore.loadBlackList(anyLong())).willReturn(Optional.of(savedBlackList));

        //act
        var blackListItem = blackListController.getAccountBlackList(anyLong());

        //assert
        assertThat(blackListItem).isEqualTo(savedBlackList);
    }

    @DisplayName("Get blacklist with account not in blackList throws ResourceNotFound exception")
    @Test
    void getAccountWithNonExistentIdThrows() {
        //arrange
        var dataStore = mock(DataStore.class);
        var blackListController = new BlackListController(dataStore);
        given(dataStore.loadBlackList(anyLong())).willReturn(Optional.empty());

        //assert
        assertThatThrownBy(() -> blackListController.getAccountBlackList(anyLong()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("Add existing account to blacklist saves black list item to db and returns saved black list item")
    @Test
    void addExistingAccountToBlackSavesAndReturnsBlackListItem() {
        //arrange
        var savedBlackList = new SavedBlackList(1L,
                new BlackList(2L, "test reason"));
        var dataStore = mock(DataStore.class);
        var blackListController = new BlackListController(dataStore);
        var blackListRequest = new BlackListRequest(1L, "test reason");
        var accountFromDb = makeValidAccount();
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.of(accountFromDb));
        given(dataStore.saveBlackList(any(BlackList.class))).willReturn(savedBlackList);

        //act
        var blackListItem = blackListController.addAccountToBlackList(blackListRequest);

        //assert
        assertThat(blackListItem).isEqualTo(savedBlackList);
    }

    @DisplayName("Add non-existent account to blacklist throws ResourceNotFound exception")
    @Test
    void addNonExistentAccountToBlackListThrows() {
        //arrange
        var savedBlackList = new SavedBlackList(1L,
                new BlackList(2L, "test reason"));
        var dataStore = mock(DataStore.class);
        var blackListController = new BlackListController(dataStore);
        var blackListRequest = new BlackListRequest(1L, "test reason");
        given(dataStore.saveBlackList(any(BlackList.class))).willReturn(savedBlackList);

        //assert
        assertThatThrownBy(() -> blackListController.addAccountToBlackList(blackListRequest))
                .isInstanceOf(ResourceNotFoundException.class);
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
        var blackListController = new BlackListController(dataStore);
        given(dataStore.loadAccount(anyLong())).willReturn(Optional.empty());
        var blackListRequest = new BlackListRequest(1L, "test reason");

        //assert
        assertThatThrownBy(() -> blackListController.addAccountToBlackList(blackListRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @DisplayName("Remove account from black list")
    @Test
    void removeAccountFromBlackList() {
        //arrange
        var dataStore = mock(DataStore.class);
        var blackListController = new BlackListController(dataStore);
        var accountId = 123L;

        //act
        blackListController.deleteAccountBalance(accountId);

        //assert
        verify(dataStore)
                .deleteBlackList(argThat(id -> id == accountId));
    }
}