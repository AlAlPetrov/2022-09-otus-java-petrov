package ru.otus.domain;

public record TariffRequest(Long type,
                            String name,
                            Long price,
                            Long initialValue) {
}
