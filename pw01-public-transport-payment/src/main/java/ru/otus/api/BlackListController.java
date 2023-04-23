package ru.otus.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.*;
import ru.otus.domain.BlackListRequest;
import ru.otus.exception.ResourceNotFoundException;
import ru.otus.service.DataStore;

@RestController
@RequestMapping("/api/v1/blackList")
public class BlackListController {
    private static final Logger log = LoggerFactory.getLogger(BlackListController.class);
    private final DataStore dataStore;

    public BlackListController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping()
    public BlackList getAccountBlackList(@RequestParam Long accountId) {
        return  dataStore
                .loadBlackList(accountId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping()
    @ResponseStatus( HttpStatus.CREATED )
    public BlackList addAccountToBlackList(@RequestBody BlackListRequest blackListRequest) {
        dataStore
                .loadAccount(blackListRequest.accountId())
                .orElseThrow(ResourceNotFoundException::new);

        return dataStore.saveBlackList(BlackList.fromRequest(blackListRequest));
    }

    @DeleteMapping()
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void deleteAccountBalance(@RequestParam Long accountId) {
        dataStore.deleteBlackList(accountId);
    }
}
