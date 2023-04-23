package ru.otus.domain;

public record BlackListRequest(Long accountId,
                               String reason) {

}
