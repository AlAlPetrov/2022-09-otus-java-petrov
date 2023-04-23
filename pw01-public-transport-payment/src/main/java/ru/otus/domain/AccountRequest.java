package ru.otus.domain;

import java.util.List;

public record AccountRequest(String cardNumber,
                             List<AccountBalanceRequest> balances) {
}
