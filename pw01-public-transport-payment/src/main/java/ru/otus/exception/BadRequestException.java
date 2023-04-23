package ru.otus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BadRequestException extends ResponseStatusException {
    public BadRequestException(@Nullable String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
