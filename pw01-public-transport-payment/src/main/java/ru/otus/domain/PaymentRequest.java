package ru.otus.domain;

public record PaymentRequest(Long accountId,
                             Long tariffId,
                             Long value) {
}
