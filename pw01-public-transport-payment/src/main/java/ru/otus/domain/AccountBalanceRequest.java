package ru.otus.domain;

public record AccountBalanceRequest(Long tariff,
                                    Long remainingValue,
                                    Long reservedValue) {
}
