package ru.otus.exceptions;

public class ReflectionReaderException extends RuntimeException {
    public ReflectionReaderException(Class<?> classType, String message) {
        super(classType.getName() + " " + message);
    }
}
