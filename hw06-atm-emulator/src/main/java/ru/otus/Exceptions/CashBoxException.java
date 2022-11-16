package ru.otus.Exceptions;

public class CashBoxException extends RuntimeException {
    public CashBoxException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
