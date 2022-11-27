package ru.otus;

import java.util.UUID;
public record Transaction(UUID id) {
    public Transaction() {
        this(java.util.UUID.randomUUID());
    }

    @Override public boolean equals(Object other) {
        if (!(other instanceof Transaction)) {
            return false;
        }
        return id == ((Transaction)other).id;
    }

    @Override public String toString() {
        return id.toString();
    }
}
