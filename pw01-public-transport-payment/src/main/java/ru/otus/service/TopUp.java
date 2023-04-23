package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Account;
import ru.otus.domain.TopUpRequest;

@Service
public interface TopUp {
    Account topUpAccount(TopUpRequest topUpRequest);
}
