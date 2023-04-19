package ru.otus.domain;

public record TariffDto(Long type,
                        String name,
                        Long price,
                        Long initialValue) {
}
