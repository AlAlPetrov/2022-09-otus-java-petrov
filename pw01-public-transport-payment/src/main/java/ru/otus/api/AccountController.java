package ru.otus.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Account;
import ru.otus.domain.AccountBalance;
import ru.otus.domain.AccountBalanceRequest;
import ru.otus.domain.AccountRequest;
import ru.otus.service.DataStore;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final DataStore dataStore;

    public AccountController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody AccountRequest accountRequest) {
        return dataStore.saveAccount(Account.fromRequest(accountRequest));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccountById(@PathVariable("id") Long id) {
        return  dataStore
                .loadAccount(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping(value = "/{id}/balance")
    @ResponseStatus( HttpStatus.CREATED )
    public Account addBalanceToAccount(@PathVariable("id") Long id,
                                       @RequestBody AccountBalanceRequest accountBalanceRequest) {
        var account = dataStore
                .loadAccount(id)
                .orElseThrow(ResourceNotFoundException::new);

        account.addBalance(AccountBalance.fromRequest(accountBalanceRequest));
        return dataStore.saveAccount(account);
    }

    @DeleteMapping(value = "/{accountId}/balance/{id}")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void deleteAccountBalance(@PathVariable("accountId") Long accountId,
                                             @PathVariable("id") Long id) {
        var account = dataStore
                .loadAccount(accountId)
                .orElseThrow(ResourceNotFoundException::new);

        account.removeBalance(id);
        dataStore.saveAccount(account);
    }

}
