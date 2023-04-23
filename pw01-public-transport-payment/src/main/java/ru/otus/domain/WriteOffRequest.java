package ru.otus.domain;

public record WriteOffRequest(Long accountId,
                              Long tariffId,
                              Long value) {
}
