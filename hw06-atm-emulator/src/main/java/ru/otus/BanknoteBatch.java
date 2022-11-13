package ru.otus;

public record BanknoteBatch(int banknoteValue, int count, int total) {
    public BanknoteBatch(int banknoteValue, int count) {
        this(banknoteValue, count, banknoteValue * count);
    }
};
