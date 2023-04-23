package ru.otus.domain;

public record TopUpRequest(Long accountId,
                           Long tariffId,
                           Long value) {
}
