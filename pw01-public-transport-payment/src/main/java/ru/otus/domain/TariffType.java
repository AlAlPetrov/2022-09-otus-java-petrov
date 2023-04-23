package ru.otus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.PersistenceCreator;
import reactor.util.annotation.NonNull;

@Table("tarifftypes")
public class TariffType {
    @Id
    private final Long id;

    @NonNull
    private final String name;

    @PersistenceCreator
    public TariffType(Long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public TariffType(@NonNull String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TariffType{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
