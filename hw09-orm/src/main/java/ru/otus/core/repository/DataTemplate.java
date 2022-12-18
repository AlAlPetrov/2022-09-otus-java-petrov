package ru.otus.core.repository;

import java.sql.Connection;
import java.util.List;

public interface DataTemplate<T> {
    T findById(Connection connection, long id);

    List<T> findAll(Connection connection);

    long insert(Connection connection, T object);

    void update(Connection connection, T object);
}
